package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class RespuestaActividad {
    private int id_ptt;
    private int id_actividadarea;
    private int id_area;
    private int id_actividad;
    private String fecha_respuesta;
    private String hora_respuesta;
    private int estado_respuesta;
    private int conformidad_respuesta;
    private int rangorecepcion_respuesta;
    private String observacion_respuesta;
    private String foto_nombre_antes;
    private String foto_base_antes;
    private String foto_nombre_despues;
    private String foto_base_despues;
    private String ejecutor_actividad_respuesta;
    private int id_supervisor;
    private int id_respuesta_server;
    private int estado_envio_respuesta;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_ptt", id_ptt);
        values.put("id_actividadarea", id_actividadarea);
        values.put("id_area", id_area);
        values.put("id_actividad", id_actividad);
        values.put("fecha_respuesta", fecha_respuesta);
        values.put("hora_respuesta", hora_respuesta);
        values.put("estado_respuesta", estado_respuesta);
        values.put("conformidad_respuesta", conformidad_respuesta);
        values.put("rangorecepcion_respuesta", rangorecepcion_respuesta);
        values.put("observacion_respuesta", observacion_respuesta);
        values.put("foto_nombre_antes", foto_nombre_antes);
        //values.put("foto_base_antes", foto_base_antes);
        values.put("foto_nombre_despues", foto_nombre_despues);
        //values.put("foto_base_despues", foto_base_despues);
        values.put("ejecutor_actividad_respuesta", ejecutor_actividad_respuesta);
        values.put("id_supervisor", id_supervisor);
        values.put("id_respuesta_server", id_respuesta_server);
        values.put("estado_envio_respuesta", estado_envio_respuesta);
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

    public String getFecha_respuesta() {
        return fecha_respuesta;
    }
    public void setFecha_respuesta(String fechaRespuesta) {
        fecha_respuesta = fechaRespuesta;
    }

    public String getHora_respuesta() {
        return hora_respuesta;
    }
    public void setHora_respuesta(String horaRespuesta) {
        hora_respuesta = horaRespuesta;
    }

    public int getEstado_respuesta() {
        return estado_respuesta;
    }
    public void setEstado_respuesta(int estadoRespuesta) {
        estado_respuesta = estadoRespuesta;
    }

    public int getConformidad_respuesta() {
        return conformidad_respuesta;
    }
    public void setConformidad_respuesta(int conformidadRespuesta) {
        conformidad_respuesta = conformidadRespuesta;
    }

    public int getRangorecepcion_respuesta() {
        return rangorecepcion_respuesta;
    }
    public void setRangorecepcion_respuesta(int rangoRecepcionRespuesta) {
        rangorecepcion_respuesta = rangoRecepcionRespuesta;
    }

    public String getObservacion_respuesta() {
        return observacion_respuesta;
    }
    public void setObservacion_respuesta(String observacionRespuesta) {
        observacion_respuesta = observacionRespuesta;
    }

    public String getFoto_nombre_antes() {
        return foto_nombre_antes;
    }
    public void setFoto_nombre_antes(String nomFotoAntes) {
        foto_nombre_antes = nomFotoAntes;
    }

    public String getFoto_base_antes() {
        return foto_base_antes;
    }
    public void setFoto_base_antes(String base64FotoAntes) {
        foto_base_antes = base64FotoAntes;
    }

    public String getFoto_nombre_despues() {
        return foto_nombre_despues;
    }
    public void setFoto_nombre_despues(String nomFotoDespues) {
        foto_nombre_despues = nomFotoDespues;
    }

    public String getFoto_base_despues() {
        return foto_base_despues;
    }
    public void setFoto_base_despues(String base64FotoDespues) {
        foto_base_despues = base64FotoDespues;
    }

    public String getEjecutor_actividad_respuesta() {
        return ejecutor_actividad_respuesta;
    }
    public void setEjecutor_actividad_respuesta(String ejecutorActividadRespuesta) {
        ejecutor_actividad_respuesta = ejecutorActividadRespuesta;
    }

    public int getId_supervisor() {
        return id_supervisor;
    }
    public void setId_supervisor(int idSupervisor) {
        id_supervisor = idSupervisor;
    }

    public int getEstado_envio_respuesta() {
        return estado_envio_respuesta;
    }
    public void setEstado_envio_respuesta(int estadoEnvioRespuesta) {
        estado_envio_respuesta = estadoEnvioRespuesta;
    }

    public int getId_respuesta_server() {
        return id_respuesta_server;
    }

    public void setId_respuesta_server(int id_respuesta_server) {
        this.id_respuesta_server = id_respuesta_server;
    }
}

