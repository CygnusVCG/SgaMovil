package com.cygnus.sgamovil.sqlite;

/**
 * Created by joan.mortheiru on 28/02/2018.
 */

import android.provider.BaseColumns;

/**
 * Esquema de la base de datos para las cuentas
 */

public class EstructuraDbParametrosUsuario {

    public static abstract class ParametrosUsuarioDb implements BaseColumns {
        public static final String TABLE_NAME ="Parametros_Usuario";

        public static final String usr_folio = "usr_folio";
        public static final String usr_monitoreo_mail = "usr_monitoreo_mail";
        public static final String usr_monitoreo_gps = "usr_monitoreo_gps";
        public static final String usr_toma_fotos = "usr_toma_fotos";
        public static final String usr_version = "usr_version";
    }
}

