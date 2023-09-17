package com.cygnus.sgamovil.sqlite;

import android.provider.BaseColumns;

public class EstructuraDbRespuestas {
    public static abstract class RespuestasDb implements BaseColumns {
        public static final String TABLE_NAME ="Respuestas_Diaria";

        public static final String id_ptt = "id_ptt";
        public static final String id_actividadarea = "id_actividadarea";
        public static final String id_area = "id_area";
        public static final String id_actividad = "id_actividad";
        public static final String fecha_respuesta = "fecha_respuesta";
        public static final String hora_respuesta = "hora_respuesta";
        public static final String estado_respuesta = "estado_respuesta";
        public static final String conformidad_respuesta = "conformidad_respuesta";
        public static final String rangorecepcion_respuesta = "rangorecepcion_respuesta";
        public static final String observacion_respuesta = "observacion_respuesta";
        public static final String foto_nombre_antes = "foto_nombre_antes";
        public static final String foto_base64_antes = "foto_base64_antes";
        public static final String foto_nombre_despues = "foto_nombre_despues";
        public static final String foto_base64_despues = "foto_base64_despues";
        public static final String ejecutor_actividad_respuesta = "ejecutor_actividad_respuesta";
        public static final String id_supervisor = "id_supervisor";
        public static final String id_respuesta_server = "id_respuesta_server";
        public static final String estado_envio_respuesta = "estado_envio_respuesta";
    }
}
