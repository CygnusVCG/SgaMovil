package com.cygnus.sgamovil.cls;

import android.content.ContentValues;


/**
 * Created by joan.mortheiru on 19/01/2018.
 */
public class Log {

    private int UsuarioLog;
    private String FechaLog;
    private String HoraLog;
    private int TipoLog;
    private String AccionLog;
    private String MensajeLog;
    private int EnvioLog;

    public Log(){    }

    public Log(int usuario, String fecha, String hora, int tipo, String accion, String mensaje, int envio){
        this.UsuarioLog = usuario;
        this.FechaLog = fecha;
        this.HoraLog = hora;
        this.TipoLog = tipo;
        this.AccionLog = accion;
        this.MensajeLog = mensaje;
        this.EnvioLog = envio;
    }

    public int getUsuarioLog(){
        return UsuarioLog;
    }

    public void setUsuarioLog(int valor){
        this.UsuarioLog = valor;
    }

    public String getFechaLog(){
        return FechaLog;
    }

    public void setFechaLog(String valor){
        this.FechaLog = valor;
    }

    public String getHoraLog(){
        return HoraLog;
    }

    public void setHoraLog(String valor){
        this.HoraLog = valor;
    }

    public int getTipoLog(){
        return TipoLog;
    }

    public void setTipoLog(int valor){
        this.TipoLog = valor;
    }

    public String getAccionLog(){
        return AccionLog;
    }

    public void setAccionLog(String valor){
        this.AccionLog = valor;
    }

    public String getMensajeLog(){
        return MensajeLog;
    }

    public void setMensajeLog(String valor){
        this.MensajeLog = valor;
    }

    public int getEnvioLog(){
        return EnvioLog;
    }

    public void setEnvioLog(int valor){
        this.EnvioLog = valor;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("usr_log", UsuarioLog);
        values.put("fecha_log", FechaLog);
        values.put("hora_log", HoraLog);
        values.put("tipo_log", TipoLog);
        values.put("accion_log", AccionLog);
        values.put("mensaje_log", MensajeLog);
        values.put("envio_log", EnvioLog);
        return values;
    }

}