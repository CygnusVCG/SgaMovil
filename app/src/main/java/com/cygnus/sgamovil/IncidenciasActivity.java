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

import com.cygnus.sgamovil.cls.IncidenciasAdapter;
import com.cygnus.sgamovil.cls.IncidenciasDiarias;
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

public class IncidenciasActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ListView listview;
    private ArrayList<IncidenciasDiarias> incidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicidencias);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Incidencias Diarias");
        super.setNombreUsuario();

        listview = (ListView) findViewById(R.id.lstIncidencias);

        traeIncidencias();
        //incidencias = Usuarios.incidencias;
    }

    @Override
    public void onClick(View v) {
    }

    private void traeIncidencias(){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            String strWS = "https://b.sga.cygnus.cl/Movil/TraeIncidenciasDiaria?usr=" + Usuarios.getUsr_autoid();
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
                                ArrayList<IncidenciasDiarias> incidenciasReportadas  = new ArrayList<>();
                                IncidenciasDiarias incid;
                                for(int i=0; i< jsonArray.length(); i++){

                                    System.out.println(jsonArray.getJSONObject(i));
                                    System.out.println(" ID incidencia : "+ jsonArray.getJSONObject(i).getInt("id_incidencia"));

                                    if(jsonArray.getJSONObject(i).getInt("estado") != 15){
                                        incid = new IncidenciasDiarias();
                                        incid.setId_incidencia(jsonArray.getJSONObject(i).getInt("id_incidencia"));
                                        incid.setId_area(jsonArray.getJSONObject(i).getInt("id_area"));
                                        incid.setNombre_area(jsonArray.getJSONObject(i).getString("nombre_area"));
                                        incid.setComentario(jsonArray.getJSONObject(i).getString("comentario"));
                                        incid.setNivel_conformidad(jsonArray.getJSONObject(i).getInt("nivel_conformidad"));
                                        incid.setConformidad(jsonArray.getJSONObject(i).getString("conformidad"));
                                        incid.setFoto(jsonArray.getJSONObject(i).getString("foto"));
                                        incid.setEstado(jsonArray.getJSONObject(i).getInt("estado"));
                                        incid.setEstado_incidencia(jsonArray.getJSONObject(i).getString("estado_incidencia"));
                                        incid.setHora_Incidencia(jsonArray.getJSONObject(i).getString("timestamp"));
                                        incid.setId_personaempresa(jsonArray.getJSONObject(i).getInt("id_personaempresa"));
                                        incid.setNombre_persona(jsonArray.getJSONObject(i).getString("nombre_persona"));
                                        incid.setPaterno_personasempresa(jsonArray.getJSONObject(i).getString("paterno_personasempresa"));
                                        incid.setMaterno_personasempresa(jsonArray.getJSONObject(i).getString("materno_personasempresa"));
                                        incid.setId_empresa(jsonArray.getJSONObject(i).getInt("id_empresa"));
                                        incid.setNombre_empresa(jsonArray.getJSONObject(i).getString("nombre_empresa"));
                                        incid.setTipo_incidencia(jsonArray.getJSONObject(i).getInt("tipo_incidencia"));
                                        incid.setTipo(jsonArray.getJSONObject(i).getString("tipo"));

                                        incidenciasReportadas.add(incid);

                                        System.out.println("Id_incidencia: "+incid.getId_incidencia());
                                        System.out.println("Comentario: "+incid.getComentario());
                                    }
                                }
                                incidencias = incidenciasReportadas;
                                armaIncidencias();
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

    private void armaIncidencias(){
        System.out.println("======================================================");
        System.out.println("Incidencias Largo: "+incidencias.size());
        for(int i=0;i<incidencias.size();i++){
            System.out.println("Incidencia ("+i+"): "+incidencias.get(i).getId_incidencia());
        }
        System.out.println("======================================================");

        IncidenciasAdapter myAdapter = new IncidenciasAdapter(this, R.layout.item_incidencia, incidencias);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(IncidenciasActivity.this, DetalleIncidenciaActivity.class);
                intent.putExtra("id_incidencia", incidencias.get(position).getId_incidencia());
                intent.putExtra("nombre_area", incidencias.get(position).getNombre_area());
                intent.putExtra("comentario", incidencias.get(position).getComentario());
                intent.putExtra("foto", incidencias.get(position).getFoto());
                intent.putExtra("tipo", incidencias.get(position).getTipo());
                intent.putExtra("nombre_persona", incidencias.get(position).getNombre_persona());
                intent.putExtra("paterno_personasempresa", incidencias.get(position).getPaterno_personasempresa());
                intent.putExtra("conformidad", incidencias.get(position).getConformidad());
                startActivity(intent);
                //Toast.makeText(ProgramacionActivity.this, "Has pulsado: "+ programacion.get(position).getId_ptt(), Toast.LENGTH_LONG).show();

            }
        });
    }
}