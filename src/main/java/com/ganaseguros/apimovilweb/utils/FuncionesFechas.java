package com.ganaseguros.apimovilweb.utils;

public class FuncionesFechas {
    public static String formatearFecha_ddmmyyyy(String fecha) {
        try {
            String[] fechaString = fecha.split("-");
            String dia = fechaString[2].split("T")[0]+"";
            String mes = fechaString[1]+"";
            String anio = fechaString[0]+"";
            return dia+"/"+mes+"/"+anio;

        } catch (Exception ex) {
            return "";
        }
    }
}
