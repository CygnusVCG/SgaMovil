package com.cygnus.sgamovil.cls;

import android.content.Context;

import com.cygnus.sgamovil.sqlite.LogDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IngresoLog {

    public IngresoLog(){}

    public static void generaLog(Context contexto, int usuario, int tipo, String accion, String mensaje){

        // Tipo 1: Informativo
        // Tipo 2: Error
        // Tipo 3: Exito

        Date anotherCurDate = new Date();
        SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");//"d/M/yyyy");
        SimpleDateFormat formatterHora = new SimpleDateFormat("hh:mm");
        String formatoDateString = formatterDia.format(anotherCurDate);
        String formatoTimeString = formatterHora.format(anotherCurDate);

        Log logLogin = new Log(Usuarios.usr_autoid, formatoDateString, formatoTimeString,1, accion, mensaje, 0);
        LogDbHelper catDb = new LogDbHelper(contexto);
        catDb.guardar(logLogin);

    }

}

