package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class ProgramacionDiaria {
    public int id_ptt;
    public int id_actividadarea;
    public int id_programacion;
    public String fecha_programacion;
    public String descripcion_programacion;
    public String hora_inicio;
    public String hora_termino;

    public int id_ejecucion;
    public String descripcion_ejecucion;
    public String observacion_tarea;

    public int id_tarea;
    public String titulo_tarea;
    public String descripcion_tarea;
    public int id_area;
    public int id_actividad;
    public String nombre_actividad;
    public String descripcion_actividad;
    public String materiales_actividad;
    public int id_personasempresa;
    public int id_usuario_interno;

    public int tipo_tarea;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_ptt", id_ptt);
        values.put("id_actividadarea", id_actividadarea);
        values.put("id_programacion", id_programacion);
        values.put("fecha_programacion", fecha_programacion);
        values.put("descripcion_programacion", descripcion_programacion);
        values.put("hora_inicio", hora_inicio);
        values.put("hora_termino", hora_termino);

        values.put("id_ejecucion", id_ejecucion);
        values.put("descripcion_ejecucion", descripcion_ejecucion);
        values.put("observacion_tarea", observacion_tarea);

        values.put("id_tarea", id_tarea);
        values.put("titulo_tarea", titulo_tarea);
        values.put("descripcion_tarea", descripcion_tarea);
        values.put("id_area", id_area);
        values.put("id_actividad", id_actividad);
        values.put("nombre_actividad", nombre_actividad);
        values.put("descripcion_actividad", descripcion_actividad);
        values.put("materiales_actividad", materiales_actividad);
        values.put("id_personasempresa", id_personasempresa);
        values.put("id_usuario_interno", id_usuario_interno);
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

    public int getId_programacion() {
        return id_programacion;
    }
    public void setId_programacion(int idProgramacion) {
        id_programacion = idProgramacion;
    }

    public int getId_tarea() {
        return id_tarea;
    }
    public void setId_tarea(int idTarea) {
        id_tarea = idTarea;
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

    public int getId_personasempresa() {
        return id_personasempresa;
    }
    public void setId_personasempresa(int idPersonasempresa) {
        id_personasempresa = idPersonasempresa;
    }

    public int getId_usuario_interno() {
        return id_usuario_interno;
    }
    public void setId_usuario_interno(int idUsuarioInterno) {
        id_usuario_interno = idUsuarioInterno;
    }

    public String getFecha_programacion() {
        return fecha_programacion;
    }

    public void setFecha_programacion(String fechaProgramacion) {
        fecha_programacion = fechaProgramacion;
    }

    public String getDescripcion_programacion() {
        return descripcion_programacion;
    }

    public void setDescripcion_programacion(String descripcionProgramacion) {
        descripcion_programacion = descripcionProgramacion;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String horaInicio) {
        hora_inicio = horaInicio;
    }

    public String getHora_termino() {
        return hora_termino;
    }

    public void setHora_termino(String horaTermino) {
        hora_termino = horaTermino;
    }

    public String getTitulo_tarea() {
        return titulo_tarea;
    }

    public void setTitulo_tarea(String tituloTarea) {
        titulo_tarea = tituloTarea;
    }

    public String getDescripcion_tarea() {
        return descripcion_tarea;
    }

    public void setDescripcion_tarea(String descripcionTarea) {
        descripcion_tarea = descripcionTarea;
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

    public int getId_ejecucion() {
        return id_ejecucion;
    }
    public void setId_ejecucion(int idEjecucion) {
        id_ejecucion = idEjecucion;
    }

    public String getDescripcion_ejecucion() {
        return descripcion_ejecucion;
    }
    public void setDescripcion_ejecucion(String descripcionEjecucion) { descripcion_ejecucion = descripcionEjecucion; }

    public String getObservacion_tarea() {
        return observacion_tarea;
    }
    public void setObservacion_tarea(String observacionTarea) { observacion_tarea = observacionTarea; }

    public int getTipo_tarea() {
        return tipo_tarea;
    }
    public void setTipo_tarea(int tipoTarea) {
        tipo_tarea = tipoTarea;
    }
}
