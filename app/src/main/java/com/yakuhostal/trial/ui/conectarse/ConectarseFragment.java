package com.yakuhostal.trial.ui.conectarse;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.yakuhostal.trial.R;


public class ConectarseFragment extends Fragment {

    private ConectarseViewModel conectarseViewModel;
    private static final int RC_SIGN_IN = 12345 ;
    public GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    public SignInButton sign_in_button;
    public LinearLayout signOutAndDisconnect;
    public TextView tvMostrarUsuario;
    public TextView tvUsuario;
    public Button signOutButton;
    private FirebaseFirestore db;
    private RecyclerView rvConectarseReservas;
    private DatabaseReference ReservasRef;
    private FirebaseAuth mAuthor;
    private Button butDelete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_conectarse, container, false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        sign_in_button = root.findViewById(R.id.sign_in_button);
        tvMostrarUsuario = root.findViewById(R.id.tvMostrarUsuario);
        signOutButton = root.findViewById(R.id.signOutButton);
        butDelete=root.findViewById(R.id.butDelete);
         cerrarSesion();
        signOutButton.setVisibility(View.GONE);
        tvUsuario = root.findViewById(R.id.tvUsuario);
        tvUsuario.setVisibility(View.GONE);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        cerrarSesion();
                        break;


                }
            }
        });

        return root;
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("miFiltro", "firebaseAuthWithGoogle:" + account.getId());
//                Log.d("miFiltro", "firebaseAuthWithGoogleToken:" + account.getIdToken());
                cerrarSesion();
                handleSignInResult(task);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                cerrarSesion();
                Log.w("miFiltro", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("miFiltro", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            cerrarSesion();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("miFiltro", "signInWithCredential:failure", task.getException());
                            cerrarSesion();
                            updateUI(null);
                        }

                    }
                });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            cerrarSesion();
            // Signed in successfully, show authenticated UI.
            updateUIGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("miFiltro", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){Log.w("miFiltro", "onStart:Email usuario registrado: " + currentUser.getEmail());}
        updateUI(currentUser);

    }


    private void updateUIGoogle(GoogleSignInAccount user) {


        if (user != null) {
//            Toast.makeText(getActivity(), "Authentication ok.",
//                    Toast.LENGTH_SHORT).show();
            sign_in_button.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            tvUsuario.setVisibility(View.VISIBLE);
            tvMostrarUsuario.setText(user.getEmail());
            Log.w("miFiltro", "updateUI Email usuario registrado: " + user.getEmail());
            MostrarReservas mReservas = new MostrarReservas();
            mReservas.execute();
            cerrarSesion();
        } else {
            Log.w("miFiltro", "updateUI Email usuario registrado: " + user);


            Toast.makeText(getActivity(), getString(R.string.autentificando),
                    Toast.LENGTH_SHORT).show();

        }
    }


    private void updateUI(FirebaseUser user) {


        if (user != null) {
//            Toast.makeText(getActivity(), "Authentication ok.",
//                    Toast.LENGTH_SHORT).show();
            sign_in_button.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            tvUsuario.setVisibility(View.VISIBLE);
            tvMostrarUsuario.setText(user.getEmail());
            Log.w("miFiltro", "updateUI Email usuario registrado: " + user.getEmail());
           MostrarReservas mReservas = new MostrarReservas();
           mReservas.execute();
            cerrarSesion();
        } else {
            Log.w("miFiltro", "updateUI Email usuario registrado: " + user);

            Toast.makeText(getActivity(), getString(R.string.autentificando),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public class MostrarReservas extends AsyncTask<Object, Integer, Integer> {
        //Variables necesarias en el hilo
        private int res;
        private ProgressDialog progreso;
        //Esta funcionalidad es en la que se va ha hacer la inserción
        @Override
        protected Integer doInBackground(Object[] objects) {

            db= FirebaseFirestore.getInstance();

             FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser!=null) {

//                FirebaseAuth mAuthss;
//                mAuthss = FirebaseAuth.getInstance();
//                FirebaseUser currentUser = mAuthss.getCurrentUser();
//                if(currentUser!=null){

                String tituloApp =  getResources().getString(R.string.tituloApp).concat("  "+currentUser.getEmail());
                Log.d("miFiltro2","Titulo App-> "+tituloApp);
                String tituloApps = tituloApp.replace("Bienvenido a Yaku Hostal", "Bienvenido a Yaku Hostal " + currentUser.getEmail());
                    Log.d("miFiltro2","Titulo Appss-> "+tituloApps);

//                }
                final String Email =currentUser.getEmail();
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference reservasRef = rootRef.collection("Reservas");
                reservasRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayMap<String, String> arrayMap = new ArrayMap<>();
                            int cantidadReservas = 0;
                            String correoAdmin=getString(R.string.correoAdmin).toLowerCase();
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("miFiltro","Esto es correo document->"+String.valueOf(document.getString("Correo Electronico").toLowerCase()));
//                                Log.d("miFiltro","Esto es correo auth->"+String.valueOf(String.valueOf(Email).toLowerCase()));
                                if(String.valueOf(Email).toLowerCase().equals(correoAdmin)){
                                    Log.d("miFiltro","Nombre: "+ document.getString("Nombre"));
                                    Log.d("miFiltro","FechaEntrada: "+ document.getString("Fecha de entrada"));
                                    Log.d("miFiltro","FechaSalida: "+ document.getString("Fecha de salida"));

                                    cantidadReservas++;

                                    arrayMap.put("Nombre"+cantidadReservas,document.getString("Nombre"));
                                    arrayMap.put("FechaEntrada"+cantidadReservas,  document.getString("Fecha de entrada"));
                                    arrayMap.put("FechaSalida"+cantidadReservas,  document.getString("Fecha de salida"));
                                    arrayMap.put("CorreoElectronico"+cantidadReservas,document.getString("Correo Electronico"));
                                    arrayMap.put("NumeroPersonas"+cantidadReservas, String.valueOf(document.getDouble("Numero de Personas")));
                                    arrayMap.put("NumeroTelefono"+cantidadReservas,  document.getString("Numero de telefono"));
                                    String feche = document.getString("Fecha de entrada");
                                    String email = document.getString("Correo Electronico");

                                    Log.d("miFiltro","feche");
                                    Log.d("miFiltro",feche);
                                    feche= feche.replace("/","_*_");
                                    String idReserva=(email+"_+_"+feche).toLowerCase();

                                    arrayMap.put("documentId"+cantidadReservas, idReserva);

                                    MiThreadConectarseReservas miThreadConectarseReservas = new MiThreadConectarseReservas(arrayMap,cantidadReservas,getActivity());
                                    miThreadConectarseReservas.start();
                                }else if(String.valueOf(document.getString("Correo Electronico").toLowerCase()).contains(String.valueOf(Email).toLowerCase())){
                                    Log.d("miFiltro","Nombre: "+ document.getString("Nombre"));
                                    Log.d("miFiltro","FechaEntrada: "+ document.getString("Fecha de entrada"));
                                    Log.d("miFiltro","FechaSalida: "+ document.getString("Fecha de salida"));

                                    cantidadReservas++;

                                    arrayMap.put("Nombre"+cantidadReservas,document.getString("Nombre"));
                                    arrayMap.put("FechaEntrada"+cantidadReservas,  document.getString("Fecha de entrada"));
                                    arrayMap.put("FechaSalida"+cantidadReservas,  document.getString("Fecha de salida"));
                                    arrayMap.put("CorreoElectronico"+cantidadReservas,document.getString("Correo Electronico"));
                                    arrayMap.put("NumeroPersonas"+cantidadReservas, String.valueOf(document.getDouble("Numero de Personas")));
                                    arrayMap.put("NumeroTelefono"+cantidadReservas,  document.getString("Numero de telefono"));
                                    String feche = document.getString("Fecha de entrada");
                                    String email = document.getString("Correo Electronico");

                                    Log.d("miFiltro","feche");
                                    Log.d("miFiltro",feche);
                                    feche= feche.replace("/","_*_");
                                    String idReserva=(email+"_+_"+feche).toLowerCase();

                                    arrayMap.put("documentId"+cantidadReservas, idReserva);

                                    MiThreadConectarseReservas miThreadConectarseReservas = new MiThreadConectarseReservas(arrayMap,cantidadReservas,getActivity());
                                    miThreadConectarseReservas.start();
                                }

                            }


                        } else {
                            Log.d("miFiltro", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
                return null;

        }
        //cuando acaba la inserción sacamos un toast
        @Override
        protected void onPostExecute(Integer res) {
            Toast.makeText(getContext(), getString(R.string.cargandoReservas), Toast.LENGTH_SHORT).show();
        }


    }
    public class MiThreadConectarseReservas extends Thread {
        private ArrayMap<String, String> arrayMap;
        private int cantidadReservas;
        private FragmentActivity getActivity;

        public MiThreadConectarseReservas(ArrayMap<String, String> arrayMap, int cantidadReservas, FragmentActivity getActivity) {
            this.arrayMap = arrayMap;
            this.cantidadReservas=cantidadReservas;
            this.getActivity=getActivity;
        }
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAuthor= FirebaseAuth.getInstance();
                    String currentUserId = mAuthor.getCurrentUser().getUid();
                    String Email="";
                    if(mAuth.getCurrentUser()!=null){
                        Email =mAuth.getCurrentUser().getEmail();
//                        Log.d("miFiltro","esto ESSSSSSSSSSSS"+Email);
                    }
//                    CollectionReference reservas=db.collection("reservas");
                    rvConectarseReservas=getView().findViewById(R.id.rvConectarseReservas);
//                    rvConectarseReservas.setHasFixedSize(true);
                    Log.d("miFiltro","Thread Nombre: "+ arrayMap.get("Nombre1"));
                    Log.d("miFiltro","Thread FechaSalida: "+ arrayMap.get("Fecha de salida1"));
                    Log.d("miFiltro","Thread Nombre: "+ arrayMap.get("Nombre2"));
                    Log.d("miFiltro","Thread FechaSalida: "+ arrayMap.get("Fecha de salida2"));


                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                    rvConectarseReservas.setLayoutManager(mLayoutManager);
                    rvConectarseReservas.setItemAnimator(new DefaultItemAnimator());
                    rvConectarseReservas.addItemDecoration(new DividerItemDecoration(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL));
                    ConectarseReservasAdapter mAdapter = new ConectarseReservasAdapter(arrayMap,cantidadReservas,getView(),getActivity);

                    rvConectarseReservas.setAdapter(mAdapter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvConectarseReservas.setVisibility(View.VISIBLE);
                        }
                    }, 3000);

                }

            });
        }
    }
    private void cerrarSesion() {
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGoogleSignInClient.signOut();
                mGoogleSignInClient.revokeAccess();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                mAuth.signOut();

//                FirebaseAuth.getInstance().signOut();

                updateUI(null);
                updateUIGoogle(null);
//                currentUser.delete();
//                Log.w("miFiltro", "REVOKE y SIGN OUT Email usuario registrado: " + currentUser.getEmail());
                tvMostrarUsuario.setText("");
                sign_in_button.setVisibility(View.VISIBLE);
                tvUsuario.setVisibility(View.GONE);
                signOutButton.setVisibility(View.GONE);
                rvConectarseReservas.setVisibility(View.GONE);
            }
        });

    }

}