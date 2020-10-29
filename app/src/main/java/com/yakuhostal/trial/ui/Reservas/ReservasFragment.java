package com.yakuhostal.trial.ui.Reservas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yakuhostal.trial.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class ReservasFragment extends Fragment {

    private FirebaseFirestore mibasededatos;
    private TextView tvMuestraFechaEntrada,tvMuestraFechaSalida;
    private EditText etNombre, etCorreo, etNumGent,etTelefono;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";


    //Calendario para obtener fecha & hora
    private final Calendar c = Calendar.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_reservas, container, false);
        //final TextView textView = root.findViewById(R.id.text_conectarse);

        tvMuestraFechaEntrada=root.findViewById(R.id.tvMostrarFechaEntrada);
        tvMuestraFechaSalida=root.findViewById(R.id.tvMostrarFechaSalida);
        etNombre=root.findViewById(R.id.etNombre);
        etCorreo=root.findViewById(R.id.etCorreo);
        ImageButton ibFechaEntrada = root.findViewById(R.id.ibFechaEntrada);
        ImageButton ibFechaSalida = root.findViewById(R.id.ibFechaSalida);
        Button butReservar = root.findViewById(R.id.butReservar);
        mibasededatos= FirebaseFirestore.getInstance();
        etNumGent=root.findViewById(R.id.etNumGent);
        etTelefono=root.findViewById(R.id.etTelefono);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user==null){
            ibFechaEntrada.setEnabled(false);
            ibFechaSalida.setEnabled(false);
            etNombre.setEnabled(false);
            etCorreo.setEnabled(false);
            etTelefono.setEnabled(false);
            butReservar.setEnabled(false);
            etNumGent.setEnabled(false);

            Toast.makeText(getContext(),getString(R.string.textoUsuarioNoLogeadoToast),Toast.LENGTH_LONG).show();


        }
        else{
            FirebaseAuth mAuths = FirebaseAuth.getInstance();
            FirebaseUser usuario = mAuths.getCurrentUser();
            Log.d("miFiltro","EMAIL -->"+usuario.getEmail());
            if(user!=null){
                etCorreo.setText(String.valueOf(usuario.getEmail()));etCorreo.setEnabled(false);}

            ibFechaEntrada.setEnabled(true);
            ibFechaSalida.setEnabled(true);
            etNombre.setEnabled(true);
//            etCorreo.setEnabled(true);
            etTelefono.setEnabled(true);
            butReservar.setEnabled(true);
            etNumGent.setEnabled(true);
            Log.d("texto ","logeado");
        }
        ibFechaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFechaEntrada();
            }
        });
        ibFechaSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFechaSalida();
            }
        });




        butReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mirar si existe algun campo que este vacio
                if(etNombre.getText().equals("")||etNumGent.getText().equals("")||etTelefono.getText().equals("")||tvMuestraFechaEntrada.getText().equals("")||tvMuestraFechaSalida.getText().equals("")){
                    Toast.makeText(getContext(), getString(R.string.rellenaCampos), Toast.LENGTH_SHORT).show();


                }else{Insert insercion=new Insert();
                    insercion.execute();
                }


            }
        });
        return root;
    }


    private void mostrarFechaEntrada() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                c.set(year, month, day);
                String selectedDate = day + "/" + (month+1) + "/" + year;
                int fechaSeleccionada = c.get(Calendar.DAY_OF_WEEK);
                boolean esLunes = (fechaSeleccionada == Calendar.MONDAY);
                if (esLunes) {
                    Toast.makeText(getContext(), getString(R.string.lunesCerradoToast), Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
                        Date fechaactual = new Date(System.currentTimeMillis());
                        String salidaString = tvMuestraFechaSalida.getText().toString();
                        Date dateSalida = null;
                        if(!salidaString.equals("")){
                            Date dateSal=new SimpleDateFormat("dd/MM/yyyy").parse(salidaString);
                            dateSalida = new Date(System.currentTimeMillis());
                        }

                        if(date.after(fechaactual)) {
                            if(!salidaString.equals("")) {
                                if (date.before(dateSalida)) {
                                    tvMuestraFechaEntrada.setText(selectedDate);
                                }else{
                                    tvMuestraFechaEntrada.setText("");
                                    tvMuestraFechaSalida.setText("");

                                    Toast.makeText(getContext(), getString(R.string.lunesCerradoToast), Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                tvMuestraFechaEntrada.setText(selectedDate);

                            }
                        }
                        else {
                            comprobarFecha(selectedDate);

                            Toast.makeText(getContext(), getString(R.string.seleccioneFechaPosteriorAHoy), Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        assert getFragmentManager() != null;
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void mostrarFechaSalida() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                c.set(year, month, day);
                String selectedDate = day + "/" + (month+1) + "/" + year;
                int fechaSeleccionada = c.get(Calendar.DAY_OF_WEEK);
                boolean esLunes = (fechaSeleccionada == Calendar.MONDAY);
                boolean LunesEncontrado = false;
                if (esLunes) {
                    Toast.makeText(getContext(),getString(R.string.lunesCerradoToast), Toast.LENGTH_LONG).show();
                }
                else {
                    comprobarFecha(selectedDate);
                }
            }
        });
        assert getFragmentManager() != null;
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void comprobarFecha(String selectedDate) {
        boolean LunesEncontrado = false;
        if(!tvMuestraFechaEntrada.getText().toString().equals("")) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
                Date fechaEntrada = new SimpleDateFormat("dd/MM/yyyy").parse(tvMuestraFechaEntrada.getText().toString());


                if (date.after(fechaEntrada)) {
                    long difference = date.getTime() - fechaEntrada.getTime();
                    long days = (difference / (60 * 60 * 24 * 1000));
                    Log.d("miFiltro", "difference " + days);

                    if (days <= 6) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fechaEntrada);
                        Log.d("miFiltro", "calendarrr");

                        Calendar calendarSalida = Calendar.getInstance();
                        calendarSalida.setTime(date);
                        LunesEncontrado = false;
                        while (
                                (!String.valueOf(cal.get(Calendar.DAY_OF_WEEK)).equals(String.valueOf(Calendar.MONDAY)))
                                        && (cal.before(calendarSalida))
                        ) {
                            cal.add(Calendar.DATE, 1);
                            if (String.valueOf(cal.get(Calendar.DAY_OF_WEEK)).equals(String.valueOf(Calendar.MONDAY))) {

                                Log.d("miFiltro", "luneswhile");
                                Log.d("miFiltro", String.valueOf(LunesEncontrado));
                                LunesEncontrado = true;
                                tvMuestraFechaSalida.setText("");

                            }
                        }
                        Log.d("miFiltro", "lunes");
                        Log.d("miFiltro", String.valueOf(LunesEncontrado));
                        if (!LunesEncontrado) {
                            tvMuestraFechaSalida.setText(selectedDate);

                        }
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.salidaAnteriorAEntradaToast), Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Insert extends AsyncTask<Object, Integer, Integer> {




        @Override
        protected Integer doInBackground(Object[] objects) {

            //Aqui se guardan los datos de la aplicacion en variables
            String nom = etNombre.getText().toString();
            int numtelf = Integer.parseInt(etTelefono.getText().toString());
            String email = etCorreo.getText().toString();
            int numPer = Integer.parseInt(etNumGent.getText().toString());
            String feche = tvMuestraFechaEntrada.getText().toString();
            String fechs = tvMuestraFechaSalida.getText().toString();

            //Insercion a la BBDD
            //Aqui se guardan los datos de las variables en el mapa, poniendole un String delante,
            //El String que pongamos delante sera el nombre de la columna en la que se guardaran los datos
            Map<String, Object> datosReserva = new HashMap<>();
            datosReserva.put("Nombre", nom);
            datosReserva.put("Correo Electronico", email);
            datosReserva.put("Numero de Telefono", numtelf);
            datosReserva.put("Numero de Personas", numPer);
            datosReserva.put("Fecha de entrada", feche);
            datosReserva.put("Fecha de salida", fechs);
            mibasededatos= FirebaseFirestore.getInstance();

            //aqui se hace la subida a la BBDD
            //el String que vaya dentro de collections() sera el nombre de la tabla en la que se guardaran los datos
            //el String que vaya dentro de document() sera la ID que se le dara a la subida de datos
            Log.d("miFiltro",feche);
            feche= feche.replace("/","_*_");
            String idReserva=(email+"_+_"+feche).toLowerCase();

            mibasededatos.collection("Reservas").document(idReserva).set(datosReserva);

            SystemClock.sleep(1000);
            return null;
        }



        @Override
        protected void onPostExecute(Integer res) {
            Toast.makeText(getContext(), getString(R.string.reservaExitosaToast), Toast.LENGTH_LONG).show();

            //Vaciar campos de texto
            etNombre.setText("");
            etNumGent.setText("");
            etTelefono.setText("");
            tvMuestraFechaEntrada.setText("");
            tvMuestraFechaSalida.setText("");
        }

    }

}