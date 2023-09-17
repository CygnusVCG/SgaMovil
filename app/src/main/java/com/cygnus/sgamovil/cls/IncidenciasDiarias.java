package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class IncidenciasDiarias {
    public int id_incidencia;
    public int id_area;
    public String nombre_area;
    public String comentario;
    public int nivel_conformidad;
    public String conformidad;
    public String foto;
    public int estado;
    public String estado_incidencia;
    public String timestamp;
    public int id_personaempresa;
    public String nombre_persona;
    public String paterno_personasempresa;
    public String materno_personasempresa;
    public int id_empresa;
    public String nombre_empresa;
    public int tipo_incidencia;
    public String tipo;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_incidencia", id_incidencia);
        values.put("id_area", id_area);
        values.put("nombre_area", nombre_area);
        values.put("comentario", comentario);
        values.put("nivel_conformidad", nivel_conformidad);
        values.put("conformidad", conformidad);
        values.put("foto", foto);
        values.put("estado", estado);
        values.put("estado_incidencia", estado_incidencia);
        values.put("timestamp", timestamp);
        values.put("id_personaempresa", id_personaempresa);
        values.put("nombre_persona", nombre_persona);
        values.put("paterno_personasempresa", paterno_personasempresa);
        values.put("materno_personasempresa", materno_personasempresa);
        values.put("id_empresa", id_empresa);
        values.put("nombre_empresa", nombre_empresa);
        values.put("tipo_incidencia", tipo_incidencia);
        values.put("tipo", tipo);
        return values;
    }

    public int getId_incidencia() {
        return id_incidencia;
    }
    public void setId_incidencia(int idIncidencia) {
        id_incidencia = idIncidencia;
    }

    public int getId_area() {
        return id_area;
    }
    public void setId_area(int idArea) {
        id_area = idArea;
    }

    public String getNombre_area() {
        return nombre_area;
    }
    public void setNombre_area(String nombreArea) {
        nombre_area = nombreArea;
    }

    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comen) {
        comentario = comen;
    }

    public int getNivel_conformidad() {
        return nivel_conformidad;
    }
    public void setNivel_conformidad(int nivelConformidad) {
        nivel_conformidad = nivelConformidad;
    }

    public String getConformidad() {
        return conformidad;
    }
    public void setConformidad(String confor) {
        conformidad = confor;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String picture) {
        foto = picture;
    }

    public int getEstado() {
        return estado;
    }
    public void setEstado(int est) {
        estado = est;
    }

    public String getEstado_incidencia() {
        return estado_incidencia;
    }
    public void setEstado_incidencia(String estadoIncidencia) {
        estado_incidencia = estadoIncidencia;
    }

    public String getHora_Incidencia() {
        return timestamp;
    }
    public void setHora_Incidencia(String horaIncidencia) {
        timestamp = horaIncidencia;
    }

    public int getId_personaempresa() {
        return id_personaempresa;
    }
    public void setId_personaempresa(int idPersonaEmpresa) {
        id_personaempresa = idPersonaEmpresa;
    }

    public String getNombre_persona() {
        return nombre_persona;
    }
    public void setNombre_persona(String nombrePersona) {
        nombre_persona = nombrePersona;
    }

    public String getPaterno_personasempresa() {
        return paterno_personasempresa;
    }
    public void setPaterno_personasempresa(String paternoPersona) {
        paterno_personasempresa = paternoPersona;
    }

    public String getMaterno_personasempresa() {
        return materno_personasempresa;
    }
    public void setMaterno_personasempresa(String maternoPersona) {
        materno_personasempresa = maternoPersona;
    }

    public int getId_empresa() {
        return id_empresa;
    }
    public void setId_empresa(int idEmpresa) {
        id_empresa = idEmpresa;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }
    public void setNombre_empresa(String nombreEmpresa) {
        nombre_empresa = nombreEmpresa;
    }

    public int getTipo_incidencia() {
        return tipo_incidencia;
    }
    public void setTipo_incidencia(int tipoIncidencia) {
        tipo_incidencia = tipoIncidencia;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String nombreIncidencia) {
        tipo = nombreIncidencia;
    }
}
