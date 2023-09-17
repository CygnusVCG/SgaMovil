package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class TareaDiaria {
    public int id_ptt;
    public int id_actividadarea;
    public int id_area;
    public int id_actividad;
    public String nombre_actividad;
    public String descripcion_actividad;
    public String materiales_actividad;
    public int estado_tarea;
    public int tipo_tarea;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_ptt", id_ptt);
        values.put("id_actividadarea", id_actividadarea);
        values.put("id_area", id_area);
        values.put("id_actividad", id_actividad);
        values.put("nombre_actividad", nombre_actividad);
        values.put("descripcion_actividad", descripcion_actividad);
        values.put("materiales_actividad", materiales_actividad);
        values.put("estado_tarea", estado_tarea);
        values.put("tipo_tarea", tipo_tarea);
        return values;
    }

    public int getId_ptt() {
        return id_ptt;
    }
    public void setId_ptt(int idPtt) {
        id_ptt = idPtt;
    }

    public int getId_actividadarea() {
        return id_actividadarea;
    }
    public void setId_actividadarea(int idActividadarea) {
        id_actividadarea = idActividadarea;
    }

    public int getId_area() {
        return id_area;
    }
    public void setId_area(int idArea) {
        id_area = idArea;
    }

    public int getId_actividad() {
        return id_actividad;
    }
    public void setId_actividad(int idActividad) {
        id_actividad = idActividad;
    }

    public String getNombre_actividad() {
        return nombre_actividad;
    }

    public void setNombre_actividad(String nombreActividad) {
        nombre_actividad = nombreActividad;
    }

    public String getDescripcion_actividad() {
        return descripcion_actividad;
    }

    public void setDescripcion_actividad(String descripcionActividad) {
        descripcion_actividad = descripcionActividad;
    }

    public String getMateriales_actividad() {
        return materiales_actividad;
    }

    public void setMateriales_actividad(String materialesActividad) {
        materiales_actividad = materialesActividad;
    }

    public int getEstado_tarea() {
        return estado_tarea;
    }
    public void setEstado_tarea(int estadoTarea) {
        estado_tarea = estadoTarea;
    }

    public int getTipo_tarea() {
        return tipo_tarea;
    }
    public void setTipo_tarea(int tipoTarea) {
        tipo_tarea = tipoTarea;
    }
}
