package com.yakuhostal.trial.ui.conectarse;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yakuhostal.trial.R;
import com.yakuhostal.trial.ui.inicio.InicioFragment;


class ConectarseReservasAdapter extends RecyclerView.Adapter<ConectarseReservasAdapter.ConectarseReservasViewHolder> {

    View view;
    int cantidadReservas;
    FragmentActivity getActivity;
    ArrayMap<String, String> mArrayMap = new ArrayMap<>();
   int qReservas=0;


    public ConectarseReservasAdapter(ArrayMap<String, String> arrayMap, int cantidadReservas) {
        this.cantidadReservas=cantidadReservas;
        mArrayMap =arrayMap;

    }

    public ConectarseReservasAdapter(ArrayMap<String, String> arrayMap, int cantidadReservas, View view) {
        this.cantidadReservas=cantidadReservas;
        mArrayMap =arrayMap;
        this.view=view;
    }

    public ConectarseReservasAdapter(ArrayMap<String, String> arrayMap, int cantidadReservas, View view, FragmentActivity getActivity) {
        this.cantidadReservas=cantidadReservas;
        mArrayMap =arrayMap;
        this.view=view;
        this.getActivity=getActivity;}

    public static class ConectarseReservasViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFechaEntradaConectarse, tvFechaSalidaConectarse, tvNumeroPersonasConectarse;
        public Button butDelete;
        public ConectarseReservasViewHolder(View itemView) {

            super(itemView);


            tvFechaEntradaConectarse = itemView.findViewById(R.id.tvFechaEntradaConectarse);
            tvFechaSalidaConectarse = itemView.findViewById(R.id.tvFechaSalidaConectarse);
            tvNumeroPersonasConectarse = itemView.findViewById(R.id.tvNumeroPersonasConectarse);
            butDelete = itemView.findViewById(R.id.butDelete);



        }

    }



    public ConectarseReservasAdapter(){

    }

    @NonNull
    @Override
    public ConectarseReservasViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_conectarse_item, parent, false);

        return new ConectarseReservasViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull final ConectarseReservasViewHolder holder, int position) {

        Log.d("miFiltro","Nombre-> "+ mArrayMap.get("Nombre") +" posicion: "+position);
        qReservas++;
        holder.tvFechaEntradaConectarse.setText(mArrayMap.get("FechaEntrada"+qReservas));
        holder.tvFechaSalidaConectarse.setText(mArrayMap.get("FechaSalida"+qReservas));
        holder.tvNumeroPersonasConectarse.setText((mArrayMap.get("NumeroPersonas"+qReservas)));
        holder.butDelete.setTag((mArrayMap.get("documentId"+qReservas)));

        holder.butDelete.setOnClickListener(new View.OnClickListener() {
            private String Email;
            private String Tag;
            @Override
            public void onClick(View v) {

                Tag=v.getTag().toString();
                Button butCancelarBorrado,butConfimarBorrado;
                butCancelarBorrado=view.findViewById(R.id.butCancelarBorrado);
                butConfimarBorrado=view.findViewById(R.id.butConfimarBorrado);
                final RelativeLayout relativeLayout;
                relativeLayout=view.findViewById(R.id.relativeLayout);
                final ConstraintLayout consLayConfirCancel;
                consLayConfirCancel = view.findViewById(R.id.consLayConfirCancel);
                consLayConfirCancel.setVisibility(View.VISIBLE);
                TextView tvMostrarReservarBorrado;
                tvMostrarReservarBorrado = view.findViewById(R.id.tvMostrarReservarBorrado);
                tvMostrarReservarBorrado.setText("Correo Electronico"+Tag.replace("_*_","/").replace("_","").replace("+"," con fecha de entrada: "));
                relativeLayout.setVisibility(View.INVISIBLE);

                butConfimarBorrado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        db.collection("Reservas").document(Tag)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("miFiltro", "DocumentSnapshot successfully deleted!");
                                        consLayConfirCancel.setVisibility(View.GONE);

                                        Toast.makeText(holder.itemView.getContext(), "Reserva cancelada", Toast.LENGTH_LONG).show();
                                        relativeLayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(holder.itemView.getContext(), "Recarge la pagina para ver sus reservas", Toast.LENGTH_LONG).show();

//                                        getActivity.getSupportFragmentManager()
//                                                .beginTransaction()
//                                                .replace(R.id.container, new InicioFragment())
//                                                .addToBackStack(null)
//                                                .commit();
//                                        getSupportFragmentManager
//                                        view.getContext().setContentView(R.layout.fragment_inicio);


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("miFiltro", "Error deleting document", e);
                                    }
                                });
                    }
                });
//
                butCancelarBorrado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(holder.itemView.getContext(), "Cancelacion de reserva cancelada", Toast.LENGTH_LONG).show();
                        consLayConfirCancel.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);

                    }
                });


            }
        });
        Log.d("miFiltro","reservasId-> "+ holder.butDelete.getTag());

    }
    @Override

    public int getItemCount() {

        Log.d("miFiltro","Cantidad: "+cantidadReservas );

        return cantidadReservas;

    }

}

