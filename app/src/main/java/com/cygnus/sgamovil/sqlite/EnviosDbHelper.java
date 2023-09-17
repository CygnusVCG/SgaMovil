package com.cygnus.sgamovil.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cygnus.sgamovil.cls.Envios;

public class EnviosDbHelper  extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EnvioCapturas";

    public EnviosDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EstructuraDbEnvios.EnviosDb.TABLE_NAME + " ("
                + EstructuraDbEnvios.EnviosDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EstructuraDbEnvios.EnviosDb.fecha_envio + " VARCHAR(10) NOT NULL,"
                + EstructuraDbEnvios.EnviosDb.mes_envio + " INTEGER NOT NULL,"
                + EstructuraDbEnvios.EnviosDb.regs_envio + " INTEGER NOT NULL,"
                + EstructuraDbEnvios.EnviosDb.regs_grabados + " INTEGER NOT NULL,"
                + EstructuraDbEnvios.EnviosDb.fecha_hora_envio + " TEXT NOT NULL,"
                + EstructuraDbEnvios.EnviosDb.estado_envio + " INTEGER NOT NULL,"
                + EstructuraDbEnvios.EnviosDb.tipo_envio + " TEXT NOT NULL,"
                + "UNIQUE (" + EstructuraDbEnvios.EnviosDb.fecha_envio + ", "+ EstructuraDbEnvios.EnviosDb.fecha_hora_envio +"))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void limpiar(){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(" delete from " + DATABASE_NAME + ";");
    }

    public long guardar(Envios env){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.insert(DATABASE_NAME,null,env.toContentValues());
    }

    public Cursor traer(String query, String[] parametros){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.rawQuery(query,parametros);
    }
}
