package com.cygnus.sgamovil;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cygnus.sgamovil.cls.Global;
import com.cygnus.sgamovil.cls.IngresoLog;
import com.cygnus.sgamovil.cls.MultiSpinner;
import com.cygnus.sgamovil.cls.PathUtil;
import com.cygnus.sgamovil.cls.RespuestaActividad;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.loopj.android.http.AsyncHttpClient.log;

public class AgregarActividadActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MultiSpinner.MultiSpinnerListener {

    private String[] arrayArea;
    private String[] arrayActividad;
    private String[] arrayEstado;
    private String[] arrayConformidad;
    private String[] arrayRango;
    private List<String> ejecutores;
    private static final int perCam = 100;
    private static final int perdisk = 101;
    Bitmap bitmap;
    private Button guardaDatos, fotoAntes, fotoDespues;
    private TextView txtFotoAntes, txtFotoDespues;
    private EditText txtObservaciones;
    private Spinner area, actividad, estado, conformidad, rango;
    private MultiSpinner personasturno;

    private String APP_DIRECTORY = "fotos/";

    private String rutaImagen;

    private static final int SELECT_FILE_ANTES = 1;
    private static final int SELECT_FILE_DESPUES = 2;
    private static final int SELECT_CAMARA_ANTES = 3;
    private static final int SELECT_CAMARA_DESPUES = 4;
    private String fotoAntesPath = "";
    private String fotoDespuesPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Agrega Actividad Diaria");
        super.setNombreUsuario();

        llenaAreas();
        llenaActividades();
        llenaEstados();
        llenaConformidades();
        llenaRangos();
        llenaPersonasTurno();

        guardaDatos = (Button) findViewById(R.id.btnGuardaDatos);
        guardaDatos.setOnClickListener(this);

        txtObservaciones = (EditText) findViewById(R.id.txtObervaciones);

        area =(Spinner) findViewById(R.id.spinnerArea);
        actividad = (Spinner) findViewById(R.id.spinnerActividad);
        estado =(Spinner) findViewById(R.id.spinnerEstado);
        conformidad = (Spinner) findViewById(R.id.spinnerConformidad);
        rango =(Spinner) findViewById(R.id.spinnerRango);
        personasturno = (MultiSpinner) findViewById(R.id.spinnerEjecutor);

        fotoAntes = (Button) findViewById(R.id.btnFotoAntes);
        fotoAntes.setOnClickListener(this);

        fotoDespues = (Button) findViewById(R.id.btnFotoDespues);
        fotoDespues.setOnClickListener(this);

        txtFotoAntes = (TextView) findViewById(R.id.txtFotoAntes);
        txtFotoDespues = (TextView) findViewById(R.id.txtFotoDespues);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnGuardaDatos) {
            System.out.println("-----GuardaDatos-----");
            String personasSel = "";
            boolean[] b = personasturno.getItems();
            for(int i=0;i<b.length;i++){
                System.out.println("Seleccionado ["+i+"] : "+b[i]);
                System.out.println("Seleccionado Nombre: "+Global.personasTurno[i].getNombre());
                System.out.println("Seleccionado Id: "+Global.personasTurno[i].getId());
                personasSel = personasSel + Global.personasTurno[i].getId() + "-";
            }
            System.out.println("Personas Seleccionadas: "+personasSel);

            String observaciones = txtObservaciones.getText().toString();
            System.out.println("Observacion: "+observaciones);

            int areaSel = area.getSelectedItemPosition();
            int actividadSel = actividad.getSelectedItemPosition();
            int estadoSel = estado.getSelectedItemPosition();
            int conformidadSel = conformidad.getSelectedItemPosition();
            int rangoSel = rango.getSelectedItemPosition();
            int personaturnoSel = personasturno.getSelectedItemPosition();

            int areaTarea = Global.areas[areaSel].getId();
            int actividadTarea = Global.actividades[actividadSel].getId();
            int estadoTarea = Global.estadoTarea[estadoSel].getId();
            int nivelConformidad = Global.nivelConformidad[conformidadSel].getId();
            int rangoRecepcion = Global.rangoRecepcion[rangoSel].getId();

            String picAntes = txtFotoAntes.getText().toString();
            String picDespues = txtFotoDespues.getText().toString();

            IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, " AgActiv FotoAntes", picAntes);
            IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "AgActiv FotoDespues", picDespues);

            if(areaTarea != -1 && actividadTarea != -1 && estadoTarea != -1 && nivelConformidad != -1 && rangoRecepcion != -1 && personasSel.length()>0){

                int ejecutorTarea = Global.personasTurno[personaturnoSel].getId();

                System.out.println("Estado: "+estadoTarea);
                System.out.println("Nivel: "+nivelConformidad);
                System.out.println("Rango: "+rangoRecepcion);
                System.out.println("Ejecutor: "+ejecutorTarea);

                System.out.println("Area: "+areaTarea);
                System.out.println("Actividad: "+actividadTarea);

                RespuestasDbHelper respDb = new RespuestasDbHelper(AgregarActividadActivity.this);
                RespuestaActividad resp = new RespuestaActividad();

                Date anotherCurDate = new Date();
                SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
                String fechaRespuesta = formatterDia.format(anotherCurDate);

                SimpleDateFormat formatterHora = new SimpleDateFormat("hh:mm");
                String horaRespuesta = formatterHora.format(anotherCurDate);

                resp.setId_ptt(0);
                resp.setId_actividadarea(0);
                resp.setId_area(areaTarea);
                resp.setId_actividad(actividadTarea);
                resp.setFecha_respuesta(fechaRespuesta);
                resp.setHora_respuesta(horaRespuesta);
                resp.setEstado_respuesta(estadoTarea);
                resp.setConformidad_respuesta(nivelConformidad);
                resp.setRangorecepcion_respuesta(rangoRecepcion);
                resp.setObservacion_respuesta(observaciones);

                IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "AgActiv Area", ""+areaTarea);
                IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "AgActiv Actividad", ""+actividadTarea);

                IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "AgActiv FotoAntesPath", fotoAntesPath);
                IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "AgActiv FotoDespuesPath", fotoDespuesPath);

                String horaFotos = horaRespuesta.replace(':','_');

                if(fotoAntesPath.length() > 0) {
                    String fotoAntesFinal = redimensionaFoto(fotoAntesPath, "aa_antes", horaFotos, actividadTarea);
                    resp.setFoto_nombre_antes(fotoAntesFinal);
                }else{
                    resp.setFoto_nombre_antes("");
                }
                if(fotoDespuesPath.length() > 0) {
                    String fotoDespuesFinal = redimensionaFoto(fotoDespuesPath, "aa_despues", horaFotos, actividadTarea);
                    resp.setFoto_nombre_despues(fotoDespuesFinal);
                }else{
                    resp.setFoto_nombre_despues("");
                }

                resp.setFoto_base_antes("");
                resp.setFoto_base_despues("");

                resp.setEjecutor_actividad_respuesta(personasSel);
                resp.setId_supervisor(Usuarios.usr_autoid);
                resp.setEstado_envio_respuesta(0);

                long aux = respDb.guardar(resp);

                if (aux > 0) {
                    Toast.makeText(this, "Respuesta guardada correctamente", Toast.LENGTH_LONG).show();
                    // Log
                    IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 1, "GuardaRespuesta", "Guardo respuesta (" + aux + ")");
                } else {
                    Toast.makeText(this, "Error al guardar la respuesta", Toast.LENGTH_LONG).show();
                    // Log
                    IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 1, "GuardaRespuesta", "No Guardo respuesta (" + aux + ")");
                }

                txtObservaciones.setText("");
                txtFotoAntes.setText("");
                txtFotoDespues.setText("");

            }else{
                Toast.makeText(this, "Debe seleccionar todos los valores!", Toast.LENGTH_LONG).show();
            }
        }

        if(v.getId()==R.id.btnFotoAntes){
            final CharSequence[] options = {"Tomar Foto","Elegir de galeria","Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarActividadActivity.this);
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
        }

        if(v.getId()==R.id.btnFotoDespues){
            final CharSequence[] options = {"Tomar Foto","Elegir de galeria","Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarActividadActivity.this);
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
        }
    }

    private void llenaAreas(){

        if (Global.areas != null) {

            String[] estados = new String[Global.areas.length];
            for (int i = 0; i < Global.areas.length; i++) {
                estados[i] = Global.areas[i].getNombre();
            }

            this.arrayArea = estados;
            Spinner s = (Spinner) findViewById(R.id.spinnerArea);

            ArrayAdapter<String> spinnerAreaAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayArea
            );
            spinnerAreaAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerAreaAdapter);
        }
    }

    private void llenaActividades(){

        if (Global.actividades != null) {

            String[] estados = new String[Global.actividades.length];
            for (int i = 0; i < Global.actividades.length; i++) {
                estados[i] = Global.actividades[i].getNombre();
            }

            this.arrayActividad = estados;
            Spinner s = (Spinner) findViewById(R.id.spinnerActividad);

            ArrayAdapter<String> spinnerActividadAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayActividad
            );
            spinnerActividadAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerActividadAdapter);
        }
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
        }
    }

    private void llenaPersonasTurno(){

        if (Global.personasTurno != null) {

            ejecutores = new ArrayList<String>();
            boolean[] seleccionados = new boolean[Global.personasTurno.length];
            for (int i = 1; i < Global.personasTurno.length; i++) {
                //tipo[i] = Global.personasTurno[i].getNombre();
                ejecutores.add(Global.personasTurno[i].getNombre());
                seleccionados[i] = false;
            }

            MultiSpinner s = (MultiSpinner) findViewById(R.id.spinnerEjecutor);
            s.setItems(ejecutores, "", this);
        }
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        /*for(int i=0;i<selected.length;i++){
            System.out.println("Seleccionado ["+i+"] : "+selected[i]);
        }*/
    }
    public void abrirCamaraAntesP(){
        OutputStream fos = null;
        File file = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();

            String fileName = System.currentTimeMillis()+"imagenAntes";
            values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH,"pictures/SGA");
            values.put(MediaStore.Images.Media.IS_PENDING,1);

            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri imageUri = resolver.insert(collection,values);

            try {
                fos = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING,0);
            resolver.update(imageUri,values,null,null);
        }else{
            String imageDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).toString();
            String fileName = System.currentTimeMillis()+"jpg";
            file = new File(imageDir,fileName);
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        if(saved){
            Toast.makeText(this, "Captura tomada", Toast.LENGTH_SHORT).show();
        }
        if(fos!=null){
            try{
                fos.flush();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }



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
            if(ContextCompat.checkSelfPermission(AgregarActividadActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                abrirCamaraAntes();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(AgregarActividadActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},perdisk);
            }

        }
    }
    private void permisosCamara(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(AgregarActividadActivity.this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                permisosDirectorios();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadActivity.this,Manifest.permission.CAMERA)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(AgregarActividadActivity.this,new String[]{Manifest.permission.CAMERA},perCam);
            }

        }

    }
    private void permisosDirectoriosDespues(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if(ContextCompat.checkSelfPermission(AgregarActividadActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                abrirCamaraDespues();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(AgregarActividadActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},perdisk);
            }

        }
    }
    private void permisosCamaraDespues(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(AgregarActividadActivity.this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                log.i("tag","Permisos listos");
                permisosDirectoriosDespues();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadActivity.this,Manifest.permission.CAMERA)){
                    log.i("tag","Permisos denegados");
                }else{

                }
                ActivityCompat.requestPermissions(AgregarActividadActivity.this,new String[]{Manifest.permission.CAMERA},perCam);
            }

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == perCam){
            if(permissions.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                permisosDirectorios();
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadActivity.this,Manifest.permission.CAMERA)){
                    new AlertDialog.Builder(this).setMessage("Es necesario activar los permisos de la camara, para obtener fotografias")
                            .setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(AgregarActividadActivity.this,new String[]{Manifest.permission.CAMERA},perCam);
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
                if(ActivityCompat.shouldShowRequestPermissionRationale(AgregarActividadActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    new AlertDialog.Builder(this).setMessage("Es necesario activar los permisos para guardar imagenes")
                            .setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(AgregarActividadActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},perdisk);
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



    public void abrirGaleriaAntes(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(MediaStore.Images.Media.RELATIVE_PATH));
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE_ANTES);
    }

    public void abrirGaleriaDespues(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE_DESPUES);
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

    private String redimensionaFoto(String pathFoto, String tipo, String horaFoto, int actividad){

        String pathFinal = "";

        try {
            String nombreFoto = tipo + "_" + horaFoto + "_" + actividad +".jpg";

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
            IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "Path Foto f", f.getAbsolutePath());

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
                    IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "Path Foto fs", fs.getAbsolutePath());

                    if(fs.exists()) {
                        pathFinal = fs.getAbsolutePath();
                    }else{
                        if(fs.createNewFile()){
                            FileOutputStream fo = new FileOutputStream(fs);
                            fo.write(bytes.toByteArray());
                            fo.close();

                            pathFinal = fs.getAbsolutePath();
                        }else{
                            IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "No Copia Foto", pathFinal);
                        }
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            //Log.e("Camera", e.toString());
            System.out.println("----- Exception Foto ------");
            System.out.println(e.getMessage());

            IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "Exception Foto", e.getMessage());
        }

        System.out.println("----- Redimension Foto ------");
        System.out.println(pathFinal);

        IngresoLog.generaLog(AgregarActividadActivity.this, Usuarios.usr_autoid, 2, "Redimension Foto", pathFinal);

        return pathFinal;
    }
}