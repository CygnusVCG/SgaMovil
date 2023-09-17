package com.cygnus.sgamovil;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cygnus.sgamovil.cls.Global;
import com.cygnus.sgamovil.cls.IncidenciasDiarias;
import com.cygnus.sgamovil.cls.IngresoLog;
import com.cygnus.sgamovil.cls.RespuestaActividad;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class DetalleIncidenciaActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String[] arrayEstadoIncidencia;
    private String[] arrayPersonasTurno;
    private Button guardaDatos;
    private TextView txtNombreArea, txtComentario, txtFoto, txtTipo, txtNombre, txtConformidad;
    private EditText txtObservaciones;
    private Spinner estadoIncidencia, personasTurno;

    private int id_incidencia;
    private String nombre_area,comentario,foto,tipo,nombre_persona,paterno_personasempresa,conformidad;

    private int actIncidencia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidencia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Detalle de Incidencia");
        super.setNombreUsuario();

        id_incidencia = getIntent().getExtras().getInt("id_incidencia");
        nombre_area = getIntent().getExtras().getString("nombre_area");
        comentario = getIntent().getExtras().getString("comentario");
        foto = getIntent().getExtras().getString("foto");
        tipo = getIntent().getExtras().getString("tipo");
        nombre_persona = getIntent().getExtras().getString("nombre_persona");
        paterno_personasempresa = getIntent().getExtras().getString("paterno_personasempresa");
        conformidad = getIntent().getExtras().getString("conformidad");

        llenaEstadosIncidencias();
        llenaPersonasTurno();

        guardaDatos = (Button) findViewById(R.id.btnGuardaDatos);
        guardaDatos.setOnClickListener(this);

        txtNombreArea = (TextView) findViewById(R.id.txtAreaIncidencia);
        txtNombreArea.setText(nombre_area);
        txtComentario = (TextView) findViewById(R.id.txtComentario);
        txtComentario.setText(comentario);
        txtFoto = (TextView) findViewById(R.id.txtFoto);
        txtFoto.setPaintFlags(txtFoto.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtFoto.setText(foto);

        String urlFoto = "https://sga.cygnus.cl:8030/files/imagenes/"+foto;

        txtFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(urlFoto);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //txtTipo = (TextView) findViewById(R.id.txtTipoIncidencia);
        //txtTipo.setText(tipo);
        txtNombre = (TextView) findViewById(R.id.txtInformante);
        txtNombre.setText(nombre_persona+" "+paterno_personasempresa);
        //txtConformidad = (TextView) findViewById(R.id.txtConformidad);
        //txtConformidad.setText(conformidad);

        txtObservaciones = (EditText) findViewById(R.id.txtObervaciones);

        estadoIncidencia =(Spinner) findViewById(R.id.spinnerEstadoIncidencia);
        personasTurno =(Spinner) findViewById(R.id.spinnerEjecutor);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnGuardaDatos) {
            int estadoIncidenciaSel = estadoIncidencia.getSelectedItemPosition();
            int personaturnoSel = personasTurno.getSelectedItemPosition();
            int estadoTarea = Global.estadoIncidencias[estadoIncidenciaSel].getId();

            if(estadoTarea != -1){

                int ejecutorTarea = Global.personasTurno[personaturnoSel].getId();

                String observaciones = txtObservaciones.getText().toString();
                System.out.println("Observacion: "+observaciones);

                System.out.println("Estado: "+estadoTarea);
                System.out.println("Ejecutor: "+ejecutorTarea);

                Date anotherCurDate = new Date();
                SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
                String fechaRespuesta = formatterDia.format(anotherCurDate);

                SimpleDateFormat formatterHora = new SimpleDateFormat("hh:mm");
                String horaRespuesta = formatterHora.format(anotherCurDate);

                actualizaIncidencias(id_incidencia, estadoTarea, ejecutorTarea, observaciones, fechaRespuesta, horaRespuesta);

                txtObservaciones.setText("");

            }else{
                Toast.makeText(this, "Debe seleccionar todos los valores!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void actualizaIncidencias(int idIncidencia, int estado_resolucion, int usr_ejecutor, String observacion_resolucion, String fecha_resolucion, String hora_resolucion){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            String strWS = "https://b.sga.cygnus.cl/Movil/ActualizaIncidenciasDiaria?"+
                            "idIncidencia=" + idIncidencia +
                            "&estado_resolucion="+ estado_resolucion +
                            "&usr_ejecutor="+ usr_ejecutor +
                            "&observacion_resolucion="+ observacion_resolucion +
                            "&fecha_resolucion="+ fecha_resolucion +
                            "&hora_resolucion="+ hora_resolucion;
            System.out.println(strWS);

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsRESTCYGNUS/api/usuario/PDS_REPO_USR/?USR_RUT=" + Usuarios.usr_rut + "&CTA_AUTOID=0" , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post( strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        System.out.println("***********************************");
                        System.out.println("******** ACT INCIDENCIA *******");
                        System.out.println(new String(responseBody));

                        String estadoActualizacion = new String(responseBody);

                        if (Integer.valueOf(estadoActualizacion) != 0) {
                            Toast.makeText(DetalleIncidenciaActivity.this, "Incidencia actualizada correctamente", Toast.LENGTH_LONG).show();
                            // Log
                            IngresoLog.generaLog(DetalleIncidenciaActivity.this, Usuarios.usr_autoid, 1, "EnviaIncidencia", "Guardo respuesta incidencia (" + id_incidencia + ")");
                        } else {
                            Toast.makeText(DetalleIncidenciaActivity.this, "Error al guardar la respuesta de incidencia", Toast.LENGTH_LONG).show();
                            // Log
                            IngresoLog.generaLog(DetalleIncidenciaActivity.this, Usuarios.usr_autoid, 1, "EnviaIncidencia", "No Guardo respuesta (" + id_incidencia + ")");
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }catch (Exception ex){
        }
    }

    private void llenaEstadosIncidencias(){

        if (Global.estadoIncidencias != null) {

            String[] estados = new String[Global.estadoIncidencias.length];
            for (int i = 0; i < Global.estadoIncidencias.length; i++) {
                estados[i] = Global.estadoIncidencias[i].getNombre();
            }

            this.arrayEstadoIncidencia = estados;
            Spinner s = (Spinner) findViewById(R.id.spinnerEstadoIncidencia);

            ArrayAdapter<String> spinnerEstadoAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayEstadoIncidencia
            );
            spinnerEstadoAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerEstadoAdapter);
        }
    }

    private void llenaPersonasTurno(){

        if (Global.personasTurno != null) {

            String[] tipo = new String[Global.personasTurno.length];
            for (int i = 0; i < Global.personasTurno.length; i++) {
                tipo[i] = Global.personasTurno[i].getNombre();
            }

            this.arrayPersonasTurno = tipo;
            Spinner s = (Spinner) findViewById(R.id.spinnerEjecutor);

            ArrayAdapter<String> spinnerEjecutorAdapter = new ArrayAdapter<String>(
                    this,R.layout.spinner,arrayPersonasTurno
            );
            spinnerEjecutorAdapter.setDropDownViewResource(R.layout.spinner);
            s.setAdapter(spinnerEjecutorAdapter);
        }
    }
}