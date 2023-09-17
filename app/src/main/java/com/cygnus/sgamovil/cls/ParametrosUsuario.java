package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class ParametrosUsuario {
    public static String usr_folio;
    public static String usr_monitoreo_mail;
    public static String usr_monitoreo_gps;
    public static String usr_toma_fotos;
    public static String usr_version;

    public static String getUsr_folio() {
        return usr_folio;
    }

    public static void setUsr_folio(String usr_folio) {
        ParametrosUsuario.usr_folio = usr_folio;
    }

    public static String getUsr_monitoreo_mail() {
        return usr_monitoreo_mail;
    }

    public static void setUsr_monitoreo_mail(String usr_monitoreo_mail) {
        ParametrosUsuario.usr_monitoreo_mail = usr_monitoreo_mail;
    }

    public static String getUsr_monitoreo_gps() {
        return usr_monitoreo_gps;
    }

    public static void setUsr_monitoreo_gps(String usr_monitoreo_gps) {
        ParametrosUsuario.usr_monitoreo_gps = usr_monitoreo_gps;
    }

    public static String getUsr_toma_fotos() {
        return usr_toma_fotos;
    }

    public static void setUsr_toma_fotos(String usr_toma_fotos) {
        ParametrosUsuario.usr_toma_fotos = usr_toma_fotos;
    }

    public static String getUsr_version() {
        return usr_version;
    }

    public static void setUsr_version(String usr_version) {
        ParametrosUsuario.usr_version = usr_version;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("usr_folio", usr_folio);
        values.put("usr_monitoreo_mail", usr_monitoreo_mail);
        values.put("usr_monitoreo_gps", usr_monitoreo_gps);
        values.put("usr_toma_fotos", usr_toma_fotos);
        values.put("usr_version", usr_version);
        return values;
    }
}

