package com.cygnus.sgamovil.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cygnus.sgamovil.cls.ParametrosUsuario;


/**
 * Created by joan.mortheiru on 22/01/2018.
 */
public class ParametrosUsuarioDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Parametros_Usuario";

    public ParametrosUsuarioDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EstructuraDbParametrosUsuario.ParametrosUsuarioDb.TABLE_NAME + " ("
                + EstructuraDbParametrosUsuario.ParametrosUsuarioDb.usr_folio + " TEXTT NOT NULL,"
                + EstructuraDbParametrosUsuario.ParametrosUsuarioDb.usr_monitoreo_mail + " TEXT NOT NULL,"
                + EstructuraDbParametrosUsuario.ParametrosUsuarioDb.usr_monitoreo_gps + " TEXT NOT NULL,"
                + EstructuraDbParametrosUsuario.ParametrosUsuarioDb.usr_toma_fotos + " TEXT NOT NULL,"
                + EstructuraDbParametrosUsuario.ParametrosUsuarioDb.usr_version + " TEXT NOT NULL"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void limpiar(){
        SQLiteDatabase sqlDB = getWritableDatabase();
        sqlDB.execSQL(" delete from Parametros_Usuario; ");
    }

    public long guardar(ParametrosUsuario pu){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.insert("Parametros_Usuario",null,pu.toContentValues());
    }

    public Cursor traer(String query,String[] parametros){
        SQLiteDatabase sqlDB = getWritableDatabase();
        return sqlDB.rawQuery(query,parametros);
    }

}

   /*
    public static final String usr_folio = "usr_folio";
    public static final String usr_monitoreo_mail = "usr_monitoreo_mail";
    public static final String usr_monitoreo_gps = "usr_monitoreo_gps";
    public static final String usr_toma_fotos = "usr_toma_fotos";
    public static final String usr_version = "usr_version";

     */
