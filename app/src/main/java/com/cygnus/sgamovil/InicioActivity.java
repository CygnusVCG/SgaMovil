package com.cygnus.sgamovil;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cygnus.sgamovil.cls.Global;
import com.cygnus.sgamovil.cls.ParametroGlobal;
import com.cygnus.sgamovil.cls.ProgramacionDiaria;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.ProgramacionDbHelper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class InicioActivity extends  BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        traeAreas(Usuarios.getUsr_autoid());
        traeActividades(Usuarios.getUsr_autoid());
        traePersonasTurno(Usuarios.getUsr_autoid());

        llenaProgramacion();
        eliminaProgramacion();
        llenaParametros("Nivel_Conformidad");
        llenaParametros("Rango_Recepcion");
        llenaParametros("Estado_Tarea");
        llenaParametros("Estado_Incidencia");

        super.setNombreUsuario();
    }

    public void llenaProgramacion(){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            //System.out.println("Usuario : "+Usuarios.usr_rut);
            Date anotherCurDate = new Date();
            SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
            String fechaDia = formatterDia.format(anotherCurDate);

            String strWS = "https://b.sga.cygnus.cl/Movil/TraeProgramacionDiaria?fecha=" + fechaDia + "&usr=" + Usuarios.getUsr_autoid();
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
                                    System.out.println(" ID ptt : "+ jsonArray.getJSONObject(i).getInt("id_ptt"));

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

                                    System.out.println("Id_ptt: "+prog.getId_ptt());
                                    System.out.println("Id_actividadarea: "+prog.getId_actividadarea());
                                    System.out.println("Id_programacion: "+prog.getId_programacion());
                                    System.out.println("Fecha_programacion: "+prog.getFecha_programacion());
                                    System.out.println("Descripcion_programacion: "+prog.getDescripcion_programacion());
                                    System.out.println("Hora_inicio: "+prog.getHora_inicio());
                                    System.out.println("Hora_termino: "+prog.getHora_termino());
                                    System.out.println("Id_tarea: "+prog.getId_tarea());
                                    System.out.println("Titulo_tarea: "+prog.getTitulo_tarea());
                                    System.out.println("Descripcion_tarea: "+prog.getDescripcion_tarea());
                                    System.out.println("Id_area: "+prog.getId_area());
                                    System.out.println("Id_actividad: "+prog.getId_actividad());
                                    System.out.println("Nombre_actividad: "+prog.getNombre_actividad());
                                    System.out.println("Descripcion_actividad: "+prog.getDescripcion_actividad());
                                    System.out.println("Materiales_actividad: "+prog.getMateriales_actividad());
                                    System.out.println("Id_personasempresa: "+prog.getId_personasempresa());
                                    System.out.println("Id_usuario_interno: "+prog.getId_usuario_interno());

                                    System.out.println("Tipo tarea: "+prog.getTipo_tarea());

                                    ProgramacionDbHelper progDb = new ProgramacionDbHelper(InicioActivity.this);
                                    progDb.guardar(prog);
                                }
                                Usuarios.programacion = programacion;
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
            System.out.println(ex.toString());
        }
    }

    public void eliminaProgramacion(){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            System.out.println("---- Eliminar Programacion -----");

            Date anotherCurDate = new Date();
            SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
            String fechaDia = formatterDia.format(anotherCurDate);

            String strWS = "https://b.sga.cygnus.cl/Movil/TraeProgramacionDiariaEliminar?fecha=" + fechaDia + "&usr=" + Usuarios.getUsr_autoid();
            System.out.println(strWS);

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsRESTCYGNUS/api/usuario/PDS_REPO_USR/?USR_RUT=" + Usuarios.usr_rut + "&CTA_AUTOID=0" , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post( strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));

                            if (jsonArray.length() > 0){
                                //ArrayList<Integer> eliminar  = new ArrayList<>();
                                for(int i=0; i< jsonArray.length(); i++){

                                    System.out.println(jsonArray.getJSONObject(i));
                                    System.out.println(" ID ptt : "+ jsonArray.getJSONObject(i).getInt("id_ptt"));

                                    int idPtt = jsonArray.getJSONObject(i).getInt("id_ptt");

                                    ProgramacionDbHelper progDb = new ProgramacionDbHelper(InicioActivity.this);
                                    progDb.eliminar(idPtt);
                                }
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

    public void llenaParametros(String tp){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            String strWS = "https://b.sga.cygnus.cl/Movil/TraeParametros?parametros=" + tp;
            System.out.println(strWS);

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsREST_ENTELAPP/api/EntelVentasApp/PARAMETROS_ENTEL/?tipoParametro=" + tp , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post(strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            System.out.println("PARAMETROS: "+tp);
                            System.out.println(jsonArray);
                            System.out.println(jsonArray.length());

                            ParametroGlobal param;

                            if (jsonArray.length() > 0){
                                ParametroGlobal[] parametros  = new ParametroGlobal[jsonArray.length()];
                                for(int i=0; i< jsonArray.length(); i++){
                                    param = new ParametroGlobal();
                                    param.id_parametro = (jsonArray.getJSONObject(i).getInt("id_parametro"));
                                    param.nombre_parametro = (jsonArray.getJSONObject(i).getString("descripcion_parametro"));
                                    parametros[i] = param;
                                }

                                if(tp.equals("Nivel_Conformidad")){
                                    Global.nivelConformidad = parametros;
                                    System.out.println("Nivel Conformidad");
                                    System.out.println(Global.nivelConformidad);
                                }

                                if(tp.equals("Rango_Recepcion")){
                                    Global.rangoRecepcion = parametros;
                                    System.out.println("Rango Recepcion");
                                    System.out.println(Global.rangoRecepcion);
                                }

                                if(tp.equals("Estado_Tarea")){
                                    Global.estadoTarea = parametros;
                                    System.out.println("Estado Tareas");
                                    System.out.println(Global.estadoTarea);
                                }

                                if(tp.equals("Estado_Incidencia")){
                                    Global.estadoIncidencias = parametros;
                                    System.out.println("Estado Incidencia");
                                    System.out.println(Global.estadoIncidencias);
                                }
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

    public void traePersonasTurno(int user){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            //System.out.println("Usuario : "+Usuarios.usr_rut);
            String strWS = "https://b.sga.cygnus.cl/Movil/TraePersonasTurno?usr=" + user;

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsREST_ENTELAPP/api/EntelVentasApp/PARAMETROS_ENTEL/?tipoParametro=" + tp , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post(strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            //System.out.println(jsonArray);

                            ParametroGlobal param;
                            //System.out.println(jsonArray.length());
                            if (jsonArray.length() > 0){
                                ParametroGlobal[] parametros  = new ParametroGlobal[jsonArray.length()];
                                for(int i=0; i< jsonArray.length(); i++){
                                    param = new ParametroGlobal();
                                    param.id_parametro = (jsonArray.getJSONObject(i).getInt("id_personaturno"));
                                    param.nombre_parametro = (jsonArray.getJSONObject(i).getString("nombre_personaturno"));
                                    parametros[i] = param;
                                }

                                Global.personasTurno = parametros;
                                System.out.println("Personas Turno");
                                System.out.println(Global.personasTurno);
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

    public void traeAreas(int user){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            //System.out.println("Usuario : "+Usuarios.usr_rut);
            String strWS = "https://b.sga.cygnus.cl/Movil/TraeAreas?usr=" + user;
            System.out.println(strWS);

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsREST_ENTELAPP/api/EntelVentasApp/PARAMETROS_ENTEL/?tipoParametro=" + tp , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post(strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            //System.out.println(jsonArray);

                            ParametroGlobal param;
                            //System.out.println(jsonArray.length());
                            if (jsonArray.length() > 0){
                                ParametroGlobal[] parametros  = new ParametroGlobal[jsonArray.length()];
                                for(int i=0; i< jsonArray.length(); i++){
                                    param = new ParametroGlobal();
                                    param.id_parametro = (jsonArray.getJSONObject(i).getInt("id_area"));
                                    param.nombre_parametro = (jsonArray.getJSONObject(i).getString("nombre_area"));
                                    parametros[i] = param;
                                }

                                Global.areas = parametros;
                                System.out.println("Areas");
                                System.out.println(Global.areas);
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

    public void traeActividades(int user){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            //System.out.println("Usuario : "+Usuarios.usr_rut);
            String strWS = "https://b.sga.cygnus.cl/Movil/TraeActividades?usr=" + user;

            //final RequestHandle requestHandle = client.get("http://services.cygnus-est.cl/wsREST_ENTELAPP/api/EntelVentasApp/PARAMETROS_ENTEL/?tipoParametro=" + tp , new AsyncHttpResponseHandler() {
            final RequestHandle requestHandle = client.post(strWS, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            //System.out.println(jsonArray);

                            ParametroGlobal param;
                            //System.out.println(jsonArray.length());
                            if (jsonArray.length() > 0){
                                ParametroGlobal[] parametros  = new ParametroGlobal[jsonArray.length()];
                                for(int i=0; i< jsonArray.length(); i++){
                                    param = new ParametroGlobal();
                                    param.id_parametro = (jsonArray.getJSONObject(i).getInt("id_actividad"));
                                    param.nombre_parametro = (jsonArray.getJSONObject(i).getString("nombre_actividad"));
                                    parametros[i] = param;
                                }

                                Global.actividades = parametros;
                                System.out.println("Actividades");
                                System.out.println(Global.actividades);
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

}
