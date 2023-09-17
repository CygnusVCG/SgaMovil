package com.cygnus.sgamovil.sqlite;

import android.provider.BaseColumns;

public class EstructuraDbEnvios {

    public static abstract class EnviosDb implements BaseColumns {
        public static final String TABLE_NAME ="EnvioCapturas";

        public static final String fecha_envio = "fecha_envio";
        public static final String mes_envio = "mes_envio";
        public static final String regs_envio = "regs_envio";
        public static final String regs_grabados = "regs_grabados";
        public static final String fecha_hora_envio = "fecha_hora_envio";
        public static final String estado_envio = "estado_envio";
        public static final String tipo_envio = "tipo_envio";
    }
}
