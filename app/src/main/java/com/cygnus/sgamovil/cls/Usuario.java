package com.cygnus.sgamovil.cls;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class Usuario {

    public static int usr_autoid;
    public static String usr_nombre;
    public static String usr_rut;
    public static String usr_pass;
    public static String usr_mail;

    public static String formulario_activo;
    public static int visita_folio_activo;
    public static int formulario_nuevo;

    public static String activity_volver;

    public static int cta_autoid_activo;
    public static int formulario_rapido_activo;

    private static int tipo_visita;

    public static String usr_monitoreo_mail;
    public static String usr_monitoreo_gps;
    public static String usr_toma_fotos;
    public static String usr_version;
    public static String fecha_servidor;

    public static String foto_activa;

    public static  String satm_legajoactivo;
    public static  String vis_autoid_ws;
    public static  int prg_activo;
    public static  int formulario_id_activo;
    public static  int categoria_id_activo;

    //Permisos
    public static int usa_foto_galeria;
    public static int las_foto_galeria;
    public static int ocu_pds_programada;

    private static Context cntx;
    private static Boolean visita_nueva = true;

    //DIALOGO DE PROGRESO
    private static ProgressDialog procesando;

    public static final int TIMEOUT_WS = 200 * 1000;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CAMERA = 1;

    @NonNull
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @NonNull
    private static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA};
    private static final int REQUEST_LOCATION = 1;

    public static void verifyStoragePermissions(@NonNull Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyCameraPermissions(@NonNull Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CAMERA,
                    REQUEST_CAMERA
            );
        }
    }

    public static void copyStream(@NonNull InputStream input, @NonNull OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static void writeStreamToFile(@NonNull InputStream input, @NonNull File file) {
        try {
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap getPicture(@NonNull Uri selectedImage, @NonNull Context context) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }


    @NonNull
    public static ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("usr_autoid", usr_autoid);
        values.put("usr_nombre", usr_nombre);
        values.put("usr_rut", usr_rut);
        values.put("usr_pass", usr_pass);
        values.put("usr_mail", usr_mail);
        values.put("usr_folio", "0");
        values.put("usr_monitoreo_mail", usr_monitoreo_mail);
        values.put("usr_monitoreo_gps", usr_monitoreo_gps);
        values.put("usr_toma_fotos", usr_toma_fotos);
        values.put("usr_version", usr_version);
        return values;
    }
}

