package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class ParametroGlobal {

    public int id_parametro;
    public String nombre_parametro;

    public int getId() {
        return id_parametro;
    }

    public void setId(int id) {
        this.id_parametro = id;
    }

    public String getNombre() {
        return nombre_parametro;
    }

    public void setNombre(String nombre) {
        this.nombre_parametro = nombre;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_parametro", id_parametro);
        values.put("nombre_parametro", nombre_parametro);
        return values;
    }

}