package com.cygnus.sgamovil.sqlite;

import android.provider.BaseColumns;

public class EstructuraDbProgramacion {
    public static abstract class ProgramacionDb implements BaseColumns {
        public static final String TABLE_NAME ="Programacion_Diaria";

        public static final String id_ptt = "id_ptt";
        public static final String id_actividadarea = "id_actividadarea";
        public static final String id_programacion = "id_programacion";
        public static final String fecha_programacion = "fecha_programacion";
        public static final String descripcion_programacion = "descripcion_programacion";
        public static final String hora_inicio = "hora_inicio";
        public static final String hora_termino = "hora_termino";

        public static final String id_ejecucion = "id_ejecucion";
        public static final String descripcion_ejecucion = "descripcion_ejecucion";
        public static final String observacion_tarea = "observacion_tarea";

        public static final String id_tarea = "id_tarea";
        public static final String titulo_tarea = "titulo_tarea";
        public static final String descripcion_tarea = "descripcion_tarea";
        public static final String id_area = "id_area";
        public static final String id_actividad = "id_actividad";
        public static final String nombre_actividad = "nombre_actividad";
        public static final String descripcion_actividad = "descripcion_actividad";
        public static final String materiales_actividad = "materiales_actividad";
        public static final String id_personasempresa = "id_personasempresa";
        public static final String id_usuario_interno = "id_usuario_interno";

        public static final String tipo_tarea = "tipo_tarea";
    }
}