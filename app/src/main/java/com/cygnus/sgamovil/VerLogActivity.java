package com.cygnus.sgamovil;

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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cygnus.sgamovil.cls.Envios;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.EnviosDbHelper;
import com.cygnus.sgamovil.sqlite.LogDbHelper;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VerLogActivity extends  BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private Button btnConsutar, btnEnviar;
    private EditText txtFecha;
    private DatePickerDialog datePickerDialog;
    //private LinearLayout layPopup;
    //private View inflatedView;
    //private Button cerrar;

    private int conteoRegistrosEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_log);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Ver Log de Acciones");
        super.setNombreUsuario();

        btnConsutar = (Button) findViewById(R.id.btnConsultar);
        btnConsutar.setOnClickListener(this);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setBackgroundResource(R.drawable.btn_desactivado);
        btnEnviar.setEnabled(false);
        btnEnviar.setOnClickListener(this);

        txtFecha = (EditText) findViewById(R.id.txtFecha);
        txtFecha.setOnClickListener(this);

        /*txtFechaHasta = (EditText) findViewById(R.id.txtFechaHasta);
        txtFechaHasta.setOnClickListener(this);*/

        Date anotherCurDate = new Date();
        SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
        String formatoDateString = formatterDia.format(anotherCurDate);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        txtFecha.setText(formatoDateString);
        /*
        txtFechaHasta.setText(formatoDateString);*/
        //txtFecha.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);


        //layPopup = (LinearLayout) findViewById(R.id.viewPopup);

        //cerrar = (Button) findViewById(R.id.btnCerrar);
        //cerrar.setOnClickListener(this);

        //layPopup.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnConsultar){
            //System.out.println("Boton presionado : Consultar");

            LogDbHelper logDb = new LogDbHelper(VerLogActivity.this);

            String fecha = txtFecha.getText().toString();
            /*String fechaH = txtFechaHasta.getText().toString();*/
            //System.out.println("Fecha : "+fecha);
            String[] params = {fecha, "0"};

            //Cursor c = capDb.traer(" SELECT _ID, fecha_venta, hora_venta, cod_imei  FROM capturas WHERE fecha_venta >= ? AND fecha_venta <= ? AND estado_envio = 0", params);
            Cursor c = logDb.traer(" SELECT _ID, hora_log, accion_log, mensaje_log  FROM acciones WHERE fecha_log = ? AND envio_log = ?", params);

            TableLayout tb = (TableLayout) findViewById(R.id.tablaCapturados);
            //tb.removeAllViews();
            int count = tb.getChildCount();
            for (int i = 1; i < count; i++) {
                View child = tb.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }

            //System.out.println("Cantidad Registros : "+c.getCount());
            int cont = 1;

            String id;
            String horaLog;
            String accionLog;
            String mensajeLog;

            if (c.moveToFirst()) {
                do {
                    id = c.getString(0);
                    horaLog = c.getString(1);
                    accionLog = c.getString(2);
                    mensajeLog = c.getString(3);

                    System.out.println("Mensaje : "+mensajeLog);

                    TableRow fila = new TableRow(this);

                    TextView columna1 = new TextView(this);
                    columna1.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna1.setText(id);
                    columna1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna1.setGravity(Gravity.CENTER);
                    fila.addView(columna1);

                    TextView columna2 = new TextView(this);
                    columna2.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna2.setText(horaLog);
                    columna2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna2.setGravity(Gravity.CENTER);
                    fila.addView(columna2);

                    TextView columna3 = new TextView(this);
                    columna3.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna3.setText(accionLog);
                    columna3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    columna3.setGravity(Gravity.CENTER);
                    fila.addView(columna3);

                    TextView columna4 = new TextView(this);
                    columna4.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columna4.setText(mensajeLog);
                    columna4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    columna4.setGravity(Gravity.LEFT);
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
            datePickerDialog = new DatePickerDialog(VerLogActivity.this,
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

            String fecha = txtFecha.getText().toString();

            Intent envio = new Intent().setClass(VerLogActivity.this, EnvioLogActivity.class);
            envio.putExtra("fecha",fecha);
            startActivity(envio);

            TableLayout tb = (TableLayout) findViewById(R.id.tablaCapturados);
            //tb.removeAllViews();
            int count = tb.getChildCount();
            for (int i = 1; i < count; i++) {
                View child = tb.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
            }
        }
    }
}