package com.cygnus.sgamovil.cls;

import java.util.ArrayList;

public class Usuarios {

    public static int usr_autoid;
    public static String  usr_nombre;
    public static String usr_rut;
    public static String usr_mail;
    public static String pds_activo;
    public static String pds_codigo_activo;

    public static int uxs_autoid;
    public static String token;

    public static ArrayList<ProgramacionDiaria> programacion;
    public static ArrayList<IncidenciasDiarias> incidencias;

    /*public static String usr_folio;
    public static String usr_monitoreo_mail;
    public static String usr_monitoreo_gps;
    public static String usr_toma_fotos;
    public static String usr_version;
    public static String fecha_servidor;*/

    public static int getUsr_autoid() {
        return usr_autoid;
    }

    public static void setUsr_autoid(int usr_autoid) {
        usr_autoid = usr_autoid;
    }

    public static int getUxs_autoid() {
        return uxs_autoid;
    }

    public static void setUxs_autoid(int uxs_autoid) {
        uxs_autoid = uxs_autoid;
    }

    public static String getUsr_nombre() {
        return usr_nombre;
    }

    public static void setUsr_nombre(String usr_nombre) {
        usr_nombre = usr_nombre;
    }

    public static String getUsr_rut() {
        return usr_rut;
    }

    public static void setUsr_rut(String usr_rut) {
        usr_rut = usr_rut;
    }

    public static String getUsr_mail() {
        return usr_mail;
    }

    public static void setUsr_mail(String usr_mail) {
        usr_mail = usr_mail;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        token = token;
    }

}