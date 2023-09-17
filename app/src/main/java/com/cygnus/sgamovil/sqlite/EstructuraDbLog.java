package com.cygnus.sgamovil.sqlite;
/**
 * Created by joan.mortheiru on 22/01/2018.
 */

import android.provider.BaseColumns;

/**
 * Esquema de la base de datos para las categorias
 */

public class EstructuraDbLog {

    public static abstract class LogDb implements BaseColumns {
        public static final String TABLE_NAME ="acciones";

        public static final String usuario_log = "usr_log";
        public static final String fecha_log = "fecha_log";
        public static final String hora_log = "hora_log";
        public static final String tipo_log = "tipo_log";
        public static final String accion_log = "accion_log";
        public static final String mensaje_log = "mensaje_log";
        public static final String envio_log = "envio_log";
    }

}