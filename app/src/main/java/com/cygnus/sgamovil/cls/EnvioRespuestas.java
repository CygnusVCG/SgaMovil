package com.cygnus.sgamovil.cls;

public class EnvioRespuestas {

    private RespuestaActividad[] respuestas;

    public EnvioRespuestas(){}

    public EnvioRespuestas(RespuestaActividad[] resp){
        this.respuestas = resp;
    }

    public RespuestaActividad[] getCapturas(){
        return respuestas;
    }

    public void setRespuestas(RespuestaActividad[] valor){
        this.respuestas = valor;
    }

}

