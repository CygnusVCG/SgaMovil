package com.cygnus.sgamovil;

import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cygnus.sgamovil.cls.ProgramacionAdapter;
import com.cygnus.sgamovil.cls.ProgramacionDiaria;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.LogDbHelper;
import com.cygnus.sgamovil.sqlite.ProgramacionDbHelper;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProgramacionActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView prgDia;
    private ListView listview;
    private ArrayList<ProgramacionDiaria> programacion;
    private String fechaDia,fechaTitulo = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Programación Diaria");
        super.setNombreUsuario();

        String fechaProg = getIntent().getExtras().getString("fechaProgramacion");

        if(fechaProg.length() == 0){
            Date anotherCurDate = new Date();
            SimpleDateFormat formatterDia = new SimpleDateFormat("dd-MM-yyyy");
            fechaTitulo = formatterDia.format(anotherCurDate);
            fechaDia = fechaTitulo + " 0:00:00";
        }else{
            try{
                Date dateProgramacion = new SimpleDateFormat("yyyy-MM-dd").parse(fechaProg);
                SimpleDateFormat formatterDia = new SimpleDateFormat("dd-MM-yyyy");
                fechaTitulo = formatterDia.format(dateProgramacion);
                fechaDia = fechaTitulo + " 0:00:00";
            }catch(ParseException ex){
                System.out.println("Error conversion Fecha : "+ex.getMessage());
            }
        }

        prgDia = (TextView) findViewById(R.id.prgDia);
        prgDia.setText("Programacion del dia : "+fechaTitulo);

        listview = (ListView) findViewById(R.id.lstProgramacion);

        programacion = traeProgramacion(fechaDia);

        System.out.println("======================================================");
        System.out.println("Programaciones Largo: "+programacion.size());
        for(int i=0;i<programacion.size();i++){
            System.out.println("Prog ptt ("+i+"): "+programacion.get(i).getId_ptt());
        }
        System.out.println("======================================================");

        ProgramacionAdapter myAdapter = new ProgramacionAdapter(this, R.layout.item_programacion, programacion);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(ProgramacionActivity.this, TareaActivity.class);
                intent.putExtra("id_ptt", programacion.get(position).getId_ptt());
                intent.putExtra("fechaTarea", fechaTitulo);
                startActivity(intent);
                //startActivityForResult(intent,1);
                //Toast.makeText(ProgramacionActivity.this, "Has pulsado: "+ programacion.get(position).getId_ptt(), Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onClick(View v) {
    }

    private ArrayList<ProgramacionDiaria> traeProgramacion(String fechaDia){

        String usuario = String.valueOf(Usuarios.getUsr_autoid());
        String[] params = {fechaDia, usuario};

        ProgramacionDbHelper progDb = new ProgramacionDbHelper(ProgramacionActivity.this);

        Cursor c = progDb.traer("SELECT id_ptt, titulo_tarea, descripcion_tarea, hora_inicio, hora_termino, descripcion_ejecucion, observacion_tarea FROM Programacion_Diaria WHERE fecha_programacion = ? AND id_usuario_interno= ? GROUP BY id_ptt, titulo_tarea, descripcion_tarea, hora_inicio, hora_termino", params);

        System.out.println("Cantidad Registros : "+c.getCount());
        ArrayList<ProgramacionDiaria> progs = new ArrayList<>();
        //ProgramacionDiaria prog;

        if (c.moveToFirst()) {
            do {
                ProgramacionDiaria prog = new ProgramacionDiaria();

                System.out.println("Titulo Tarea : "+c.getString(1));

                prog.setId_ptt(c.getInt(0));
                prog.setTitulo_tarea(c.getString(1));
                prog.setDescripcion_tarea(c.getString(2));
                prog.setHora_inicio(c.getString(3));
                prog.setHora_termino(c.getString(4));
                prog.setDescripcion_ejecucion(c.getString(5));
                prog.setObservacion_tarea(c.getString(6));
                progs.add(prog);
            }while(c.moveToNext());
        }

        return progs;
    }
}