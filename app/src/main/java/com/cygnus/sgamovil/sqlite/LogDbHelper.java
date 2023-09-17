package com.cygnus.sgamovil.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cygnus.sgamovil.cls.Log;


/**
 * Created by joan.mortheiru on 22/01/2018.
 */
public class LogDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "acciones";

    public LogDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EstructuraDbLog.LogDb.TABLE_NAME + " ("
                + EstructuraDbLog.LogDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EstructuraDbLog.LogDb.usuario_log + " INTEGER NOT NULL,"
                + EstructuraDbLog.LogDb.fecha_log + " VARCHAR(10) NOT NULL,"
                + EstructuraDbLog.LogDb.hora_log + " VARCHAR(5) NOT NULL,"
                + EstructuraDbLog.LogDb.tipo_log + " INTEGER NOT NULL,"
                + EstructuraDbLog.LogDb.accion_log + " VARCHAR(25) NOT NULL,"
                + EstructuraDbLog.LogDb.mensaje_log + " TEXT NOT NULL,"
                + EstructuraDbLog.LogDb.envio_log + " INTEGER NOT NULL"
                //+ "UNIQUE (" + EstructuraDbLog.LogDb.usuario_id + ", "+ EstructuraDbLog.LogDb.fecha_log +" , " + EstructuraDbLog.LogDb.hora_log +")" +
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void limpiar(){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(" delete from " + DATABASE_NAME + ";");
    }

    public long guardar(Log cat){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.insert(DATABASE_NAME,null,cat.toContentValues());
    }

    public int actualizacion(ContentValues upd, String[] parametros){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.update(DATABASE_NAME, upd, "envio_log = ? and fecha_log = ?", parametros);
        //sqlDB.execSQL(query);
    }

    public Cursor traer(String query,String[] parametros){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.rawQuery(query,parametros);
    }

}