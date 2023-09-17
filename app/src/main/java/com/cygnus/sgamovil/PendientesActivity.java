package com.cygnus.sgamovil;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cygnus.sgamovil.cls.Global;
import com.cygnus.sgamovil.cls.IncidenciasAdapter;
import com.cygnus.sgamovil.cls.IncidenciasDiarias;
import com.cygnus.sgamovil.cls.PendientesAdapter;
import com.cygnus.sgamovil.cls.ProgramacionAdapter;
import com.cygnus.sgamovil.cls.ProgramacionDiaria;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.ProgramacionDbHelper;
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

public class PendientesActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ListView listview;
    private ArrayList<ProgramacionDiaria> pendientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Tareas Pendientes");
        super.setNombreUsuario();

        listview = (ListView) findViewById(R.id.lstPendientes);

        System.out.println(listview);

        traePendientes();
    }

    @Override
    public void onClick(View v) {
    }

    private void traePendientes(){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            String strWS = "https://b.sga.cygnus.cl/Movil/TraeProgramacionPendiente?usr=" + Usuarios.getUsr_autoid();
            System.out.println(strWS);

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsRESTCYGNUS/api/usuario/PDS_REPO_USR/?USR_RUT=" + Usuarios.usr_rut + "&CTA_AUTOID=0" , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post( strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));

                            //System.out.println(jsonArray.length());
                            //System.out.println(jsonArray);

                            if (jsonArray.length() > 0){
                                ArrayList<ProgramacionDiaria> programacion  = new ArrayList<>();
                                ProgramacionDiaria prog;
                                for(int i=0; i< jsonArray.length(); i++){

                                    System.out.println(jsonArray.getJSONObject(i));
                                    System.out.println(" ID pendiente : "+ jsonArray.getJSONObject(i).getInt("id_ptt"));

                                    prog = new ProgramacionDiaria();
                                    prog.setId_ptt(jsonArray.getJSONObject(i).getInt("id_ptt"));
                                    prog.setId_actividadarea(jsonArray.getJSONObject(i).getInt("id_actividadarea"));
                                    prog.setId_programacion(jsonArray.getJSONObject(i).getInt("id_programacion"));
                                    prog.setFecha_programacion(jsonArray.getJSONObject(i).getString("fecha_programacion"));
                                    prog.setDescripcion_programacion(jsonArray.getJSONObject(i).getString("descripcion_programacion"));
                                    prog.setHora_inicio(jsonArray.getJSONObject(i).getString("hora_inicio"));
                                    prog.setHora_termino(jsonArray.getJSONObject(i).getString("hora_termino"));

                                    prog.setId_ejecucion(jsonArray.getJSONObject(i).getInt("id_ejecucion"));
                                    prog.setDescripcion_ejecucion(jsonArray.getJSONObject(i).getString("descripcion_ejecucion"));
                                    prog.setObservacion_tarea(jsonArray.getJSONObject(i).getString("observacion_tarea"));

                                    prog.setId_tarea(jsonArray.getJSONObject(i).getInt("id_tarea"));
                                    prog.setTitulo_tarea(jsonArray.getJSONObject(i).getString("titulo_tarea"));
                                    prog.setDescripcion_tarea(jsonArray.getJSONObject(i).getString("descripcion_tarea"));
                                    prog.setId_area(jsonArray.getJSONObject(i).getInt("id_area"));
                                    prog.setId_actividad(jsonArray.getJSONObject(i).getInt("id_actividad"));
                                    prog.setNombre_actividad(jsonArray.getJSONObject(i).getString("nombre_actividad"));
                                    prog.setDescripcion_actividad(jsonArray.getJSONObject(i).getString("descripcion_actividad"));
                                    prog.setMateriales_actividad(jsonArray.getJSONObject(i).getString("materiales_actividad"));
                                    prog.setId_personasempresa(jsonArray.getJSONObject(i).getInt("id_personasempresa"));
                                    prog.setId_usuario_interno(jsonArray.getJSONObject(i).getInt("id_usuario_interno"));

                                    prog.setTipo_tarea(jsonArray.getJSONObject(i).getInt("tipo_tarea"));

                                    programacion.add(prog);

                                }
                                pendientes = programacion;
                                armaPendientes();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void armaPendientes(){
        System.out.println("======================================================");
        System.out.println("Pendientes Largo: "+pendientes.size());
        for(int i=0;i<pendientes.size();i++){
            System.out.println("Pendientes ("+i+"): "+pendientes.get(i).getId_ptt());
        }
        System.out.println("======================================================");

        PendientesAdapter myAdapter = new PendientesAdapter(this, R.layout.item_tarea_v2, pendientes);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(PendientesActivity.this, RespuestaTareaActivity.class);
                intent.putExtra("id_ptt", pendientes.get(position).getId_ptt());
                intent.putExtra("id_actividadtarea", pendientes.get(position).getId_actividadarea());
                intent.putExtra("id_area", pendientes.get(position).getId_area());
                intent.putExtra("id_actividad", pendientes.get(position).getId_actividad());
                intent.putExtra("tipo_tarea", pendientes.get(position).getTipo_tarea());

                String nombreArea = traeArea(pendientes.get(position).getId_area());

                intent.putExtra("nombre_actividad", pendientes.get(position).getNombre_actividad()+ " ("+nombreArea+")");
                startActivity(intent);
            }
        });
    }

    public String traeArea(int id){

        for (int i = 0; i < Global.areas.length; i++) {
            if (Global.areas[i].getId() == id) {
                return Global.areas[i].getNombre();
            }
        }
        return "";
    }
}