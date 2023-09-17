package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class Actividad {
    public static int id_actividad;
    public static String descripcion_actividad;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_actividad", id_actividad);
        values.put("descripcion_actividad", descripcion_actividad);
        return values;
    }

    public static int getId_actividad() {
        return id_actividad;
    }
    public static void setId_actividad(int id_actividad) {
        id_actividad = id_actividad;
    }

    public static String getDescripcion_actividad() {
        return descripcion_actividad;
    }

    public static void setDescripcion_actividad(String descripcion_actividad) {
        descripcion_actividad = descripcion_actividad;
    }
}
