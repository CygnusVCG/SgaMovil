package com.cygnus.sgamovil;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cygnus.sgamovil.cls.ProgramacionDiaria;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.ProgramacionDbHelper;
import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ProgramacionAnteriorActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button btnConsutar;
    private EditText txtFecha;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion_anterior);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Programacion Anterior");
        super.setNombreUsuario();

        btnConsutar = (Button) findViewById(R.id.btnConsultar);
        btnConsutar.setOnClickListener(this);

        txtFecha = (EditText) findViewById(R.id.txtFecha);
        txtFecha.setOnClickListener(this);

        Date anotherCurDate = new Date();
        SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
        String formatoDateString = formatterDia.format(anotherCurDate);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        txtFecha.setText(formatoDateString);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConsultar) {

            RespuestasDbHelper respDb = new RespuestasDbHelper(ProgramacionAnteriorActivity.this);

            String fecha = txtFecha.getText().toString();

            llenaProgramacionAnterior(fecha);

        }

        if (v.getId() == R.id.txtFecha) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            // date picker dialog
            datePickerDialog = new DatePickerDialog(ProgramacionAnteriorActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text

                            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("d/M/yyyy");
                            String strFecha = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date fecha = null;
                            try {
                                fecha = formatoDelTexto.parse(strFecha);
                            } catch (ParseException ex) {

                            }

                            SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
                            String formatoDateString = formatterDia.format(fecha);

                            txtFecha.setText(formatoDateString);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    public void llenaProgramacionAnterior(String fechaDia){

        try {
            final AsyncHttpClient client = new AsyncHttpClient();

            //Date anotherCurDate = new Date();
            //SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
            //String fechaDia = formatterDia.format(anotherCurDate);

            String strWS = "https://b.sga.cygnus.cl/Movil/TraeProgramacionDiaria?fecha=" + fechaDia + "&usr=" + Usuarios.getUsr_autoid();
            System.out.println(strWS);

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

                                    ProgramacionDbHelper progDb = new ProgramacionDbHelper(ProgramacionAnteriorActivity.this);
                                    progDb.guardar(prog);
                                }

                                Intent intent = new Intent(ProgramacionAnteriorActivity.this, ProgramacionActivity.class);
                                intent.putExtra("fechaProgramacion", fechaDia);
                                startActivity(intent);
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