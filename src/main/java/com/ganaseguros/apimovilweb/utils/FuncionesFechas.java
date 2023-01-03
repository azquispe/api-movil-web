package com.ganaseguros.apimovilweb.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static String ConvertirDateToString(Date fecha) {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }
    public static Date ConvertirStringToDate(String fecha) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(fecha);
    }
    public static String ObtenerFechaActualParaNombreArchivo() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            String strDate = dateFormat.format(new Date());
            return strDate;
        } catch (Exception ex) {
            return null;
        }
    }
}
