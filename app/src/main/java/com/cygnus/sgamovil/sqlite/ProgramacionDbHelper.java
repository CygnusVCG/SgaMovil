package com.cygnus.sgamovil.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cygnus.sgamovil.cls.ProgramacionDiaria;

public class ProgramacionDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Programacion_Diaria";

    public ProgramacionDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EstructuraDbProgramacion.ProgramacionDb.TABLE_NAME + " ("
                + EstructuraDbProgramacion.ProgramacionDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EstructuraDbProgramacion.ProgramacionDb.id_ptt + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.id_actividadarea + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.id_programacion + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.fecha_programacion + " VARCHAR(10) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.descripcion_programacion + " VARCHAR(50) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.hora_inicio + " VARCHAR(5) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.hora_termino + " VARCHAR(5) NOT NULL,"

                + EstructuraDbProgramacion.ProgramacionDb.id_ejecucion + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.descripcion_ejecucion + " VARCHAR(20) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.observacion_tarea + " VARCHAR(60) NOT NULL,"

                + EstructuraDbProgramacion.ProgramacionDb.id_tarea + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.titulo_tarea + " VARCHAR(30) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.descripcion_tarea + " VARCHAR(100) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.id_area + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.id_actividad + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.nombre_actividad + " VARCHAR(80) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.descripcion_actividad + " VARCHAR(200) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.materiales_actividad + " VARCHAR(200) NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.id_personasempresa + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.id_usuario_interno + " INTEGER NOT NULL,"
                + EstructuraDbProgramacion.ProgramacionDb.tipo_tarea + " INTEGER NOT NULL,"
                + "UNIQUE (" + EstructuraDbProgramacion.ProgramacionDb.id_ptt + ", "+ EstructuraDbProgramacion.ProgramacionDb.id_actividadarea +")"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void limpiar(){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(" delete from Programacion_Diaria; ");
    }

    public long guardar(ProgramacionDiaria prog){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.insert("Programacion_Diaria",null,prog.toContentValues());
    }

    public Cursor traer(String query, String[] parametros){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.rawQuery(query,parametros);
    }

    public void eliminar(int id){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL("delete from Programacion_Diaria where id_ptt = "+id+"; ");
    }
}