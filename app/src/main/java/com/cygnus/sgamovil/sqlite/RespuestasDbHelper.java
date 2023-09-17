package com.cygnus.sgamovil.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cygnus.sgamovil.cls.RespuestaActividad;

public class RespuestasDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Respuestas_Diaria";

    public RespuestasDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EstructuraDbRespuestas.RespuestasDb.TABLE_NAME + " ("
                + EstructuraDbRespuestas.RespuestasDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EstructuraDbRespuestas.RespuestasDb.id_ptt + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.id_actividadarea + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.id_area + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.id_actividad + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.fecha_respuesta + " VARCHAR(10) NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.hora_respuesta + " VARCHAR(5) NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.estado_respuesta + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.conformidad_respuesta + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.rangorecepcion_respuesta + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.observacion_respuesta + " VARCHAR(100) NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.foto_nombre_antes + " VARCHAR(255) NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.foto_base64_antes + " VARCHAR(255),"
                + EstructuraDbRespuestas.RespuestasDb.foto_nombre_despues + " VARCHAR(255) NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.foto_base64_despues + " VARCHAR(255),"
                //+ EstructuraDbRespuestas.RespuestasDb.ejecutor_actividad_respuesta + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.ejecutor_actividad_respuesta + " VARCHAR(40) NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.id_supervisor + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.id_respuesta_server + " INTEGER NOT NULL,"
                + EstructuraDbRespuestas.RespuestasDb.estado_envio_respuesta + " INTEGER NOT NULL"
                //+ "UNIQUE (" + EstructuraDbLog.LogDb.usuario_id + ", "+ EstructuraDbLog.LogDb.fecha_log +" , " + EstructuraDbLog.LogDb.hora_log +")" +
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("ALTER TABLE " + EstructuraDbRespuestas.RespuestasDb.TABLE_NAME + " ADD COLUMN " + EstructuraDbRespuestas.RespuestasDb.estado_envio_respuesta + " INTEGER;");
        }catch (Exception ex){

        }

    }

    public void limpiar(){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(" delete from Respuestas_Diaria; ");
    }

    public long guardar(RespuestaActividad resp){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.insert("Respuestas_Diaria",null,resp.toContentValues());
    }

    public Cursor traer(String query, String[] parametros){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.rawQuery(query,parametros);
    }

    public void actualizar(String query){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(query);
    }

    public int actualizacion(ContentValues upd){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.update(DATABASE_NAME, upd, "estado_envio_respuesta = 0", null);
        //sqlDB.execSQL(query);
    }
}
