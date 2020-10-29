package com.yakuhostal.trial.ui.conectarse;

public class ConectarseReservasContent {


    public static class ConectarseReservasItem {

        private String nombre;
        private String fechaSalida;
        private String fechaEntrada;
        private int numPersonas;
        private int numTelefono;
        private String correoElectronico;

        public ConectarseReservasItem()
        {}

        public ConectarseReservasItem(String fechaSalida, String fechaEntrada, int numPersonas) {
            this.fechaSalida = fechaSalida;
            this.fechaEntrada = fechaEntrada;
            this.numPersonas = numPersonas;
        }

        public ConectarseReservasItem(String nombre, String fechaSalida, String fechaEntrada, int numPersonas, int numTelefono, String correoElectronico) {
            this.nombre = nombre;
            this.fechaSalida = fechaSalida;
            this.fechaEntrada = fechaEntrada;
            this.numPersonas = numPersonas;
            this.numTelefono = numTelefono;
            this.correoElectronico = correoElectronico;
        }

        public String getNombre() {
            return nombre;
        }


        public String getFechaSalida() {
            return fechaSalida;
        }


        public String getFechaEntrada() {
            return fechaEntrada;
        }


        public int getNumPersonas() {
            return numPersonas;
        }


        public int getNumTelefono() {
            return numTelefono;
        }


        public String getCorreoElectronico() {
            return correoElectronico;
        }


        public static String[] drawables;


    }

}

