package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class Area {
    public static int id_area;
    public static String descripcion_area;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_area", id_area);
        values.put("descripcion_area", descripcion_area);
        return values;
    }

    public static int getId_area() {
        return id_area;
    }
    public static void setId_area(int id_area) {
        id_area = id_area;
    }

    public static String getDescripcion_area() {
        return descripcion_area;
    }

    public static void setDescripcion_area(String descripcion_area) {
        descripcion_area = descripcion_area;
    }
}

