package com.cygnus.sgamovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;

import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
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

import com.cygnus.sgamovil.cls.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EnviarRespuestasActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button btnConsutar, btnEnviar;
    private EditText txtFecha;
    private DatePickerDialog datePickerDialog;

    private int conteoRegistrosEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_respuestas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Enviar Respuestas");
        super.setNombreUsuario();

        btnConsutar = (Button) findViewById(R.id.btnConsultar);
        btnConsutar.setOnClickListener(this);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setBackgroundResource(R.drawable.btn_desactivado);
        btnEnviar.setEnabled(false);
        btnEnviar.setOnClickListener(this);

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
        btnConsutar.performClick();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnConsultar){
            //System.out.println("Boton presionado : Consultar");

            RespuestasDbHelper respDb = new RespuestasDbHelper(EnviarRespuestasActivity.this);

            //String fechaDia = "03-02-2021";

            //Date anotherCurDate = new Date();
            //SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
            //String fechaDia = formatterDia.format(anotherCurDate);

            String fecha = txtFecha.getText().toString();
            /*String fechaH = txtFechaHasta.getText().toString();*/
            //System.out.println("Fecha : "+fecha);
            String[] params = {fecha};

            //String[] params = {fechaDia};
            System.out.println("Fecha : "+fecha);

            Cursor c = respDb.traer(" SELECT _ID, id_ptt, id_actividadarea, id_area, id_actividad, fecha_respuesta, estado_respuesta FROM Respuestas_Diaria WHERE estado_envio_respuesta = 0 AND id_respuesta_server <= 0 AND fecha_respuesta = ?", params);

            TableLayout tb = (TableLayout) findViewById(R.id.tablaCapturados);

            int count = tb.getChildCount();
            for (int i = 1; i < count; i++) {
                View child = tb.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }

            //System.out.println("Cantidad Registros : "+c.getCount());
            int cont = 1;

            if (c.moveToFirst()) {
                do {
                    String id = c.getString(0);
                    int ptt = c.getInt(1);
                    int actividadArea = c.getInt(2);
                    int area = c.getInt(3);
                    int actividad = c.getInt(4);
                    String fechaRespuesta = c.getString(5);
                    int estadoRespuesta = c.getInt(6);

                    String nombreArea = traeArea(area);
                    String nombreActividad = traeActividad(actividad);
                    String nombreEstado = traeEstado(estadoRespuesta);

                    //System.out.println("ID Registros : "+id);

                    TableRow fila = new TableRow(this);

                    /*TextView columna1 = new TextView(this);
                    columna1.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna1.setText(id);
                    columna1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna1.setGravity(Gravity.CENTER);
                    fila.addView(columna1);*/

                    TextView columna2 = new TextView(this);
                    columna2.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna2.setText(nombreArea);
                    columna2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna2.setGravity(Gravity.CENTER);
                    fila.addView(columna2);

                    TextView columna3 = new TextView(this);
                    columna3.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna3.setText(nombreActividad);
                    columna3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna3.setGravity(Gravity.CENTER);
                    fila.addView(columna3);

                    TextView columna4 = new TextView(this);
                    columna4.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna4.setText(nombreEstado);
                    columna4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna4.setGravity(Gravity.CENTER);
                    fila.addView(columna4);

                    tb.addView(fila,cont);
                    cont++;

                } while(c.moveToNext());
            }

            btnEnviar.setBackgroundResource(R.drawable.btn_enviar);
            btnEnviar.setEnabled(true);
        }

        if(v.getId() == R.id.txtFecha) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            // date picker dialog
            datePickerDialog = new DatePickerDialog(EnviarRespuestasActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text

                            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("d/M/yyyy");
                            String strFecha = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date fecha = null;
                            try{
                                fecha = formatoDelTexto.parse(strFecha);
                            }catch(ParseException ex){

                            }

                            SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
                            String formatoDateString = formatterDia.format(fecha);

                            txtFecha.setText(formatoDateString);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if(v.getId()==R.id.btnEnviar){

            btnEnviar.setBackgroundResource(R.drawable.btn_desactivado);
            btnEnviar.setEnabled(false);

            Intent envio = new Intent().setClass(EnviarRespuestasActivity.this, EnvioActivity.class);
            envio.putExtra("fecha", txtFecha.getText().toString());
            startActivity(envio);

            TableLayout tb = (TableLayout) findViewById(R.id.tablaCapturados);
            int count = tb.getChildCount();
            for (int i = 1; i < count; i++) {
                View child = tb.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }
        }
    }

    public String traeArea(int id){

        for (int i = 0; i < Global.areas.length; i++) {
            if (Global.areas[i].getId() == id) {
                return Global.areas[i].getNombre();
            }
        }
        return "";
    }

    public String traeActividad(int id){

        for (int i = 0; i < Global.actividades.length; i++) {
            if (Global.actividades[i].getId() == id) {
                return Global.actividades[i].getNombre();
            }
        }
        return "";
    }

    public String traeEstado(int id){

        for (int i = 0; i < Global.estadoTarea.length; i++) {
            if (Global.estadoTarea[i].getId() == id) {
                return Global.estadoTarea[i].getNombre();
            }
        }
        return "";
    }

}
