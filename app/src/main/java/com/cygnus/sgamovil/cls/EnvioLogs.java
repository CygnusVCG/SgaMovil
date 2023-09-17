package com.cygnus.sgamovil.cls;

public class EnvioLogs {

    private Log[] logs;

    public EnvioLogs(){}

    public EnvioLogs(Log[] acc){
        this.logs = acc;
    }

    public Log[] getLog(){
        return logs;
    }

    public void setLogs(Log[] valor){
        this.logs = valor;
    }

}
