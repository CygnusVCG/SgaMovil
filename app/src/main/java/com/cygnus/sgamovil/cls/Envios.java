package com.cygnus.sgamovil.cls;

import android.content.ContentValues;

public class Envios {
    private String fecha_envio;
    private int mes_envio;
    private int regs_enviados;
    private int regs_grabados;
    private String fecha_hora_enviado;
    private int estado_envio;
    private String tipo_envio;

    public Envios(){    }

    public Envios(String fechaEnvio, int mesEnvio, int regsEnviados, int regsGrabados, String fechaHoraEnviado, int estadoEnvio, String tipoEnvio){
        this.fecha_envio = fechaEnvio;
        this.mes_envio = mesEnvio;
        this.regs_enviados = regsEnviados;
        this.regs_grabados = regsGrabados;
        this.fecha_hora_enviado = fechaHoraEnviado;
        this.estado_envio = estadoEnvio;
        this.tipo_envio = tipoEnvio;
    }

    public String getFecha_envio(){
        return fecha_envio;
    }

    public void setFecha_envio(String valor){
        this.fecha_envio = valor;
    }

    public int getMes_envio(){
        return mes_envio;
    }

    public void setMes_envio(int valor){
        this.mes_envio = valor;
    }

    public int getRegs_enviados(){
        return regs_enviados;
    }

    public void setRegs_enviados(int valor){
        this.regs_enviados = valor;
    }

    public int getRegs_grabados(){
        return regs_grabados;
    }

    public void setRegs_grabados(int valor){
        this.regs_grabados = valor;
    }

    public String getFecha_hora_enviado(){
        return fecha_hora_enviado;
    }

    public void setFecha_hora_enviado(String valor){
        this.fecha_hora_enviado = valor;
    }

    public int getEstado_envio(){
        return estado_envio;
    }

    public void setEstado_envio(int valor){
        this.estado_envio = valor;
    }

    public String getTipoEnvio(){
        return tipo_envio;
    }

    public void setTipoEnvio(String valor){
        this.tipo_envio = valor;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("fecha_envio", fecha_envio);
        values.put("mes_envio", mes_envio);
        values.put("regs_envio", regs_enviados);
        values.put("regs_grabados", regs_grabados);
        values.put("fecha_hora_envio", fecha_hora_enviado);
        values.put("estado_envio", estado_envio);
        values.put("tipo_envio", tipo_envio);
        return values;
    }
}
