package com.cygnus.sgamovil;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cygnus.sgamovil.cls.EnvioRespuestas;
import com.cygnus.sgamovil.cls.Global;
import com.cygnus.sgamovil.cls.IngresoLog;
import com.cygnus.sgamovil.cls.MultiSpinner;
import com.cygnus.sgamovil.cls.PathUtil;
import com.cygnus.sgamovil.cls.RespuestaActividad;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.Base64;

import com.cygnus.sgamovil.cls.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.loopj.android.http.AsyncHttpClient.log;

public class RespuestaTareaActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MultiSpinner.MultiSpinnerListener {

    private String[] arrayEstado;
    private String[] arrayConformidad;
    private String[] arrayRango;
     private Button guardaDatos, fotoAntes, fotoDespues;
    private TextView txtNombreActividad, txtFotoAntes, txtFotoDespues;
    private EditText txtObservaciones;
    private Spinner estado, conformidad, rango;
     private int ptt, actarea, area, actividad, tipotarea;

    private String rutaImagen;
    private static final int SELECT_CAMARA_ANTES = 3;
    private static final int SELECT_CAMARA_DESPUES = 4;
    private static final int perCam = 100;
    private static final int perdisk = 101;
    Bitmap bitmap;

    private static final int SELECT_FILE_ANTES = 1;
    private static final int SELECT_FILE_DESPUES = 2;

    private String fotoAntesPath = "";
    private String fotoDespuesPath = "";

    private String fechaTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta_tarea);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Usuario.verifyStoragePermissions(this);

        setTitle("Detalle de Actividad");
        super.setNombreUsuario();

        ptt = getIntent().getExtras().getInt("id_ptt");
        actarea = getIntent().getExtras().getInt("id_actividadtarea");
        area = getIntent().getExtras().getInt("id_area");
        actividad = getIntent().getExtras().getInt("id_actividad");
        String nombreActividad = getIntent().getExtras().getString("nombre_actividad");
        tipotarea = getIntent().getExtras().getInt("tipo_tarea");

        fechaTarea = getIntent().getExtras().getString("fecha_tarea");

        llenaEstados();
        llenaConformidades();
        llenaRangos();

        guardaDatos = (Button) findViewById(R.id.btnGuardaDatos);
        guardaDatos.setOnClickListener(this);

        txtNombreActividad = (TextView) findViewById(R.id.txtNombreActividad);
        txtNombreActividad.setText(nombreActividad);
        txtObservaciones = (EditText) findViewById(R.id.txtObervaciones);

        estado =(Spinner) findViewById(R.id.spinnerEstado);
        conformidad = (Spinner) findViewById(R.id.spinnerConformidad);
        rango =(Spinner) findViewById(R.id.spinnerRango);
        //personasturno = (Spinner) findViewById(R.id.spinnerEjecutor);

        fotoAntes = (Button) findViewById(R.id.btnFotoAntes);
        fotoAntes.setOnClickListener(this);

        fotoDespues = (Button) findViewById(R.id.btnFotoDespues);
        fotoDespues.setOnClickListener(this);

        txtFotoAntes = (TextView) findViewById(R.id.txtFotoAntes);
        txtFotoDespues = (TextView) findViewById(R.id.txtFotoDespues);

        RespuestasDbHelper respDb = new RespuestasDbHelper(RespuestaTareaActivity.this);
        String[] params = {String.valueOf(ptt), String.valueOf(actarea)};
        Cursor c = respDb.traer(" SELECT _ID, estado_respuesta, conformidad_respuesta, rangorecepcion_respuesta, observacion_respuesta, ejecutor_actividad_respuesta, foto_nombre_antes, foto_nombre_despues FROM Respuestas_Diaria WHERE id_ptt = ? AND id_actividadarea = ?", params);

        if (c.moveToFirst()) {
            int idRespuesta = c.getInt(0);
            int estadoRespuesta = c.getInt(1);
            int conformidadRespuesta = c.getInt(2);
            int recepcionRespuesta = c.getInt(3);
            String observacionRespuesta = c.getString(4);
            //int ejecutorRespuesta = c.getInt(5);
            String ejecutorRespuesta = c.getString(5);

            String fotoAntes = c.getString(6);
            String fotoDespues = c.getString(7);

            txtObservaciones.setText(observacionRespuesta);
            txtFotoAntes.setText(fotoAntes);
            fotoAntesPath = fotoAntes;
            txtFotoDespues.setText(fotoDespues);
            fotoDespuesPath = fotoDespues;

            int posEstado = 0;
            for (int i = 0; i < Global.estadoTarea.length; i++) {
                if(Global.estadoTarea[i].getId() == estadoRespuesta)
                    posEstado = i;
            }
            estado.setSelection(posEstado);

            int posConformidad = 0;
            for (int i = 0; i < Global.nivelConformidad.length; i++) {
                if(Global.nivelConformidad[i].getId() == conformidadRespuesta)
                    posConformidad = i;
            }
            conformidad.setSelection(posConformidad);

            int posRecepcion = 0;
            for (int i = 0; i < Global.rangoRecepcion.length; i++) {
                if(Global.rangoRecepcion[i].getId() == recepcionRespuesta)
                    posRecepcion = i;
            }
            rango.setSelection(posRecepcion);

        }
        c.close();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnGuardaDatos) {
            boolean valido = false;

            System.out.println("-----GuardaDatos-----");

            String observaciones = txtObservaciones.getText().toString();
            System.out.println("Observacion: "+observaciones);

            int estadoSel = estado.getSelectedItemPosition();
            int conformidadSel = conformidad.getSelectedItemPosition();
            int rangoSel = rango.getSelectedItemPosition();

            int estadoTarea = Global.estadoTarea[estadoSel].getId();
            int nivelConformidad = Global.nivelConformidad[conformidadSel].getId();
            int rangoRecepcion = Global.rangoRecepcion[rangoSel].getId();

            String picAntes = txtFotoAntes.getText().toString();
            String picDespues = txtFotoDespues.getText().toString();

            IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "FotoAntes", picAntes);
            IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "FotoDespues", picDespues);

            if(estadoTarea != -1){
                if(estadoTarea == 9){
                    if(nivelConformidad != -1 ){
                        if(tipotarea != 1){
                            if(rangoRecepcion == -1){
                                Toast.makeText(this, "Debe ingresar rango de Recepcion!", Toast.LENGTH_LONG).show();
                                valido = false;
                            }else{
                                valido = true;
                            }
                        }else{
                            valido = true;
                        }
                    }else{
                        Toast.makeText(this, "Debe ingresar Nivel de Conformidad!", Toast.LENGTH_LONG).show();
                        valido = false;
                    }
                }else{
                    if(estadoTarea == 11 && observaciones.trim().length() == 0){
                        Toast.makeText(this, "Debe ingresar Observacion!", Toast.LENGTH_LONG).show();
                        estadoTarea = 24;
                        valido = false;
                    }else{
                        valido = true;
                    }
                }

                System.out.println("Tipo Tarea: "+tipotarea);
                System.out.println("Rango Tarea: "+rangoRecepcion);
                System.out.println("Foto Antes: "+picAntes.length());
                System.out.println("Foto Despues: "+picDespues.length());

            }else{
                Toast.makeText(this, "Debe seleccionar Estado Tarea!", Toast.LENGTH_LONG).show();
                valido = false;
            }

            System.out.println("Valido: "+valido);

            if(valido){
                if(rangoRecepcion == -1)
                    rangoRecepcion = 7;

                System.out.println("Estado: "+estadoTarea);
                System.out.println("Nivel: "+nivelConformidad);
                System.out.println("Rango: "+rangoRecepcion);

                RespuestasDbHelper respDb = new RespuestasDbHelper(RespuestaTareaActivity.this);
                RespuestaActividad resp = new RespuestaActividad();

                /// Debe ser la fecha de programacion y no de la respuesta
                try{
                    Date dateTarea = new SimpleDateFormat("dd-MM-yyyy").parse(fechaTarea);
                    SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaRespuesta = formatterDia.format(dateTarea);

                    SimpleDateFormat formatterHora = new SimpleDateFormat("hh:mm");
                    String horaRespuesta = formatterHora.format(dateTarea);

                    resp.setId_ptt(ptt);
                    resp.setId_actividadarea(actarea);
                    resp.setId_area(area);
                    resp.setId_actividad(actividad);
                    resp.setFecha_respuesta(fechaRespuesta);
                    resp.setHora_respuesta(horaRespuesta);
                    resp.setEstado_respuesta(estadoTarea);
                    resp.setConformidad_respuesta(nivelConformidad);
                    resp.setRangorecepcion_respuesta(rangoRecepcion);
                    resp.setObservacion_respuesta(observaciones);
                    resp.setId_respuesta_server(0);

                    IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "FotoAntesPath", fotoAntesPath);
                    IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "FotoDespuesPath", fotoDespuesPath);

                    if(fotoAntesPath.length() > 0) {
                        String fotoAntesFinal = redimensionaFoto(fotoAntesPath, "antes", ptt, actividad);
                        resp.setFoto_nombre_antes(fotoAntesFinal);
                    }else{
                        //if(txtFotoAntes.length()>0){
                        //    fotoAntesPath = txtFotoAntes.getText().toString();
                        //    resp.setFoto_nombre_antes(txtFotoAntes.getText().toString());
                        //}else {
                            resp.setFoto_nombre_antes("");
                        //}
                    }
                    if(fotoDespuesPath.length() > 0) {
                        String fotoDespuesFinal = redimensionaFoto(fotoDespuesPath, "despues", ptt, actividad);
                        resp.setFoto_nombre_despues(fotoDespuesFinal);
                    }else{
                        //if(fotoDespues.length()>0){
                        //    fotoDespuesPath =fotoDespues.getText().toString();
                        //    resp.setFoto_nombre_despues(fotoDespues.getText().toString());
                        //}else {
                        resp.setFoto_nombre_despues("");
                        //}
                    }

                    resp.setFoto_base_antes(" ");
                    resp.setFoto_base_despues(" ");
                    resp.setEjecutor_actividad_respuesta("");
                    resp.setId_supervisor(Usuarios.usr_autoid);
                    resp.setEstado_envio_respuesta(0);

                    String[] params = {String.valueOf(ptt), String.valueOf(actarea)};
                    Cursor c = respDb.traer(" SELECT _ID  FROM Respuestas_Diaria WHERE id_ptt = ? AND id_actividadarea = ?", params);

                    long aux = 0;

                    if (c.moveToFirst()) {

                        int idRespuesta = c.getInt(0);

                        String qryAct = "UPDATE Respuestas_Diaria SET " +
                                "hora_respuesta = '"+ horaRespuesta + "'," +
                                "estado_respuesta = "+ estadoTarea + "," +
                                "conformidad_respuesta = "+ nivelConformidad + "," +
                                "rangorecepcion_respuesta = "+ rangoRecepcion + "," +
                                "observacion_respuesta = '"+ observaciones + "'," +
                                "foto_nombre_antes = '"+fotoAntesPath+"',"+
                                "foto_base64_antes = '',"+
                                "foto_nombre_despues = '"+fotoDespuesPath+"',"+
                                "foto_base64_despues = '',"+
                                //"ejecutor_actividad_respuesta = "+ ejecutorTarea + "," +
                                "ejecutor_actividad_respuesta = ''," +
                                "id_supervisor = "+ Usuarios.usr_autoid + "," +
                                "estado_envio_respuesta = 0 " +
                                "WHERE _ID = "+idRespuesta;
                        respDb.actualizar(qryAct);
                        aux = idRespuesta;
                        //do {
                        //}while(c.moveToNext());
                    }else{
                        aux = respDb.guardar(resp);
                    }
                    c.close();

                    if (aux > 0) {
                        Toast.makeText(this, "Respuesta guardada correctamente", Toast.LENGTH_LONG).show();
                        // Log
                        IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 1, "GuardaRespuesta", "Guardo respuesta (" + aux + ")");

                        finish();
                    } else {
                        Toast.makeText(this, "Error al guardar la respuesta", Toast.LENGTH_LONG).show();
                        // Log
                        IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 1, "GuardaRespuesta", "No Guardo respuesta (" + aux + ")");
                    }

                }catch(ParseException ex){
                    System.out.println("Error conversion Fecha : "+ex.getMessage());
                }
            }
        }

        if(v.getId()==R.id.btnFotoAntes){
            final CharSequence[] options = {"Tomar Foto","Elegir de galeria","Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(RespuestaTareaActivity.this);
            builder.setTitle("Elige una opción");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int seleccion) {
                    if (options[seleccion] =="Tomar Foto"){
                        permisosCamara();
                        //abrirCamaraAntes(v);
                    } else if (options[seleccion]== "Elegir de galeria") {
                        abrirGaleriaAntes(v);
                    }
                    else if(options[seleccion] == "Cancelar"){
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
            //abrirGaleriaAntes(v);
        }

        if(v.getId()==R.id.btnFotoDespues){
            final CharSequence[] options = {"Tomar Foto","Elegir de galeria","Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(RespuestaTareaActivity.this);
            builder.setTitle("Elige una opción");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int seleccion) {
                    if (options[seleccion] =="Tomar Foto"){
                        permisosCamaraDespues();
                        //abrirCamaraAntes(v);
                    } else if (options[seleccion]== "Elegir de galeria") {
                        abrirGaleriaDespues(v);
                    }
                    else if(options[seleccion] == "Cancelar"){
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
            //abrirGaleriaDespues(v);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void llenaEstados(){

        if (Global.estadoTarea != null) {

            String[] estados = new String[Global.estadoTarea.length];
            for (int i = 0; i < Global.estadoTarea.length; i++) {
                estados[i] = Global.estadoTarea[i].getNombre();
            }

            this.arrayEstado = estados;
            Spinner s = (Spinner) findViewById(R.id.spinnerEstado);

            ArrayAdapter<String> spinnerEstadoAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayEstado
            );
            spinnerEstadoAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerEstadoAdapter);
           s.setSelection(1);
        }
    }

    private void llenaConformidades(){

        if (Global.nivelConformidad != null) {

            String[] tipo = new String[Global.nivelConformidad.length];
            for (int i = 0; i < Global.nivelConformidad.length; i++) {
                tipo[i] = Global.nivelConformidad[i].getNombre();
            }

            this.arrayConformidad = tipo;
            Spinner s = (Spinner) findViewById(R.id.spinnerConformidad);

            ArrayAdapter<String> spinnerConformidadAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayConformidad
            );
            spinnerConformidadAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerConformidadAdapter);
            s.setSelection(1);
        }
    }

    private void llenaRangos(){

        if (Global.rangoRecepcion != null) {

            String[] tipo = new String[Global.rangoRecepcion.length];
            for (int i = 0; i < Global.rangoRecepcion.length; i++) {
                tipo[i] = Global.rangoRecepcion[i].getNombre();
            }

            this.arrayRango = tipo;
            Spinner s = (Spinner) findViewById(R.id.spinnerRango);

            ArrayAdapter<String> spinnerRangoAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayRango
            );
            spinnerRangoAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerRangoAdapter);
            s.setSelection(1);
        }
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
    }

    public void abrirGaleriaAntes(View v){
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(MediaStore.Images.Media.RELATIVE_PATH));
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE_ANTES);
    }

    public void abrirGaleriaDespues(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE_DESPUES);
    }

    private void abrirCamaraAntes(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagenArchivo =  null;

        try {
            imagenArchivo = crearImagen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(imagenArchivo != null){
            Uri fotoUri = FileProvider.getUriForFile(this,"com.cygnus.sgamovil.fileprovider",imagenArchivo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
            startActivityForResult(intent,SELECT_CAMARA_ANTES);

        }
    }
    private void abrirCamaraDespues(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagenArchivo =  null;

        try {
            imagenArchivo = crearImagen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(imagenArchivo != null){
            Uri fotoUri = FileProvider.getUriForFile(this,"com.cygnus.sgamovil.fileprovider",imagenArchivo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
            startActivityForResult(intent,SELECT_CAMARA_DESPUES);

        }
    }
    public File crearImagen() throws IOException{
        String nombreImagen ="foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg",directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }
    private void permisosDirectorios(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if(ContextCompat.checkSelfPermission(RespuestaTareaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                abrirCamaraAntes();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(RespuestaTareaActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(RespuestaTareaActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},perdisk);
            }

        }
    }
    private void permisosCamara(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(RespuestaTareaActivity.this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                permisosDirectorios();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(RespuestaTareaActivity.this,Manifest.permission.CAMERA)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(RespuestaTareaActivity.this,new String[]{Manifest.permission.CAMERA},perCam);
            }

        }

    }
    private void permisosDirectoriosDespues(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if(ContextCompat.checkSelfPermission(RespuestaTareaActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                abrirCamaraDespues();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(RespuestaTareaActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(RespuestaTareaActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},perdisk);
            }

        }
    }
    private void permisosCamaraDespues(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(RespuestaTareaActivity.this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                permisosDirectoriosDespues();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(RespuestaTareaActivity.this,Manifest.permission.CAMERA)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(RespuestaTareaActivity.this,new String[]{Manifest.permission.CAMERA},perCam);
            }

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == perCam){
            if(permissions.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                permisosDirectorios();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(RespuestaTareaActivity.this,Manifest.permission.CAMERA)){
                    new AlertDialog.Builder(this).setMessage("Es necesario activar los permisos de la camara, para obtener fotografias")
                            .setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(RespuestaTareaActivity.this,new String[]{Manifest.permission.CAMERA},perCam);
                                }
                            })
                            .setNegativeButton("No gracias", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }else{
                    Toast.makeText(this,"Tendra que activar los permisos manualmente",Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(requestCode==perdisk){
            if(permissions.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                abrirCamaraAntes();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(RespuestaTareaActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    new AlertDialog.Builder(this).setMessage("Es necesario activar los permisos para guardar imagenes")
                            .setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(RespuestaTareaActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},perdisk);
                                }
                            })
                            .setNegativeButton("No gracias", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }else{
                    Toast.makeText(this,"Tendra que activar los permisos manualmente",Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        Uri selectedImage;

        String filePath = null;
        switch (requestCode) {
            case SELECT_FILE_ANTES:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();

                    try{
                        String pathFinal = PathUtil.getPath(this, selectedImage);
                        System.out.println("Uri file: "+ selectedImage.toString());

                        String s = getRealPathFromURI(selectedImage);

                        fotoAntesPath = pathFinal;
                        txtFotoAntes.setText(s);
                    }catch(URISyntaxException e){
                        System.out.println("Exception file: "+ e.getMessage());
                    }
                }
                break;
            case SELECT_CAMARA_ANTES:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap imgBitMap = BitmapFactory.decodeFile(rutaImagen);

                    try{
                        System.out.println("Uri file: "+ rutaImagen.toString());

                        fotoAntesPath = rutaImagen;
                        txtFotoAntes.setText(rutaImagen);
                    }catch(Exception e){
                        System.out.println("Exception file: "+ e.getMessage());
                    }
                }
                break;
            case SELECT_CAMARA_DESPUES:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap imgBitMap = BitmapFactory.decodeFile(rutaImagen);

                    try{
                        System.out.println("Uri file: "+ rutaImagen.toString());

                        fotoDespuesPath = rutaImagen;
                        txtFotoDespues.setText(rutaImagen);
                    }catch(Exception e){
                        System.out.println("Exception file: "+ e.getMessage());
                    }
                }
                break;
            case SELECT_FILE_DESPUES:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();

                    try{
                        String pathFinal = PathUtil.getPath(this, selectedImage);
                        System.out.println("Uri file: "+ selectedImage.toString());

                        String s = getRealPathFromURI(selectedImage);

                        fotoDespuesPath = pathFinal;
                        txtFotoDespues.setText(s);
                    }catch(URISyntaxException e){
                        System.out.println("Exception file: "+ e.getMessage());
                    }
                }
                break;
        }
    }

    public String getRealPathFromURI(Uri uri) {

        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        System.out.println(returnCursor.getString(nameIndex));
        return returnCursor.getString(nameIndex);
    }

    private String redimensionaFoto(String pathFoto, String tipo, int idptt, int actividad){

        String pathFinal = "";

        try {
            String nombreFoto = tipo + "_" + idptt + "_" + actividad +".jpg";

            //IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "RF 1", pathFoto);
            InputStream image_stream = this.getContentResolver().openInputStream(Uri.fromFile(new File(pathFoto)));
            //IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "RF 2", image_stream.toString());
            Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
            //IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "RF 3", bitmap.toString());

            int altoFoto = (int)Math.round(bitmap.getHeight() * 0.5);
            int anchoFoto = (int)Math.round(bitmap.getWidth() * 0.5);

            bitmap = Bitmap.createScaledBitmap(bitmap, anchoFoto, altoFoto, false);
            //IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "RF 4", bitmap.toString());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "RF 5", bytes.toString());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            //IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "RF 6", this.getFilesDir().getPath() + File.separator + nombreFoto);

            //File f = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + nombreFoto);
            File f = new File(this.getFilesDir().getPath() + File.separator + nombreFoto);

            //System.out.println("----- Path Foto f ------");
            //System.out.println(f.getAbsolutePath());
            IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "Path Foto f", f.getAbsolutePath());

            if(f.exists()) {
                pathFinal = f.getAbsolutePath();
            }else{
                if(f.createNewFile()){
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    pathFinal = f.getAbsolutePath();
                }else{
                    //File fs = new File(this.getFilesDir().getPath() + File.separator + nombreFoto);
                    File fs = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + nombreFoto);

                    System.out.println("----- Path Foto fs ------");
                    System.out.println(fs.getAbsolutePath());
                    IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "Path Foto fs", fs.getAbsolutePath());

                    if(fs.exists()) {
                        pathFinal = fs.getAbsolutePath();
                    }else{
                        if(fs.createNewFile()){
                            FileOutputStream fo = new FileOutputStream(fs);
                            fo.write(bytes.toByteArray());
                            fo.close();

                            pathFinal = fs.getAbsolutePath();
                        }else{
                            IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "No Copia Foto", pathFinal);
                        }
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            //Log.e("Camera", e.toString());
            System.out.println("----- Exception Foto ------");
            System.out.println(e.getMessage());

            IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "Exception Foto", e.getMessage());
        }

        System.out.println("----- Redimension Foto ------");
        System.out.println(pathFinal);

        IngresoLog.generaLog(RespuestaTareaActivity.this, Usuarios.usr_autoid, 2, "Redimension Foto", pathFinal);

        return pathFinal;
    }

}