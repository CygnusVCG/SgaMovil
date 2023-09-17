package com.cygnus.sgamovil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cygnus.sgamovil.cls.Global;
import com.cygnus.sgamovil.cls.ProgramacionDiaria;
import com.cygnus.sgamovil.cls.TareaAdapter;
import com.cygnus.sgamovil.cls.TareaAdapterV2;
import com.cygnus.sgamovil.cls.TareaDiaria;
import com.cygnus.sgamovil.sqlite.ProgramacionDbHelper;
import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TareaActivity extends BaseActivity  //AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ListView listview;
    private ArrayList<TareaDiaria> tareas;

    private String fechaTarea;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Actividades Programadas");
        super.setNombreUsuario();

        listview = (ListView) findViewById(R.id.lstTareas);

        int id_ptt = getIntent().getExtras().getInt("id_ptt");
        fechaTarea = getIntent().getExtras().getString("fechaTarea");

        tareas = traeTareas(id_ptt);

        //System.out.println("======================================================");
        //System.out.println("Tareas Largo: "+tareas.size());
        //for(int i=0;i<tareas.size();i++){
        //    System.out.println("Tarea Act Area ("+i+"): "+tareas.get(i).getId_actividadarea());
        //    System.out.println("Tarea Area ("+i+"): "+tareas.get(i).getId_area());
        //    System.out.println("Tarea Actividad ("+i+"): "+tareas.get(i).getId_actividad());
        //}
        //System.out.println("======================================================");

        TareaAdapterV2 myAdapter = new TareaAdapterV2(this, R.layout.item_tarea_v2, tareas);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(TareaActivity.this, RespuestaTareaActivity.class);
                intent.putExtra("id_ptt", tareas.get(position).getId_ptt());
                intent.putExtra("id_actividadtarea", tareas.get(position).getId_actividadarea());
                intent.putExtra("id_area", tareas.get(position).getId_area());
                intent.putExtra("id_actividad", tareas.get(position).getId_actividad());
                intent.putExtra("tipo_tarea", tareas.get(position).getTipo_tarea());

                intent.putExtra("fecha_tarea", fechaTarea);

                String nombreArea = traeArea(tareas.get(position).getId_area());

                intent.putExtra("nombre_actividad", tareas.get(position).getNombre_actividad()+ " ("+nombreArea+")");
                //startActivity(intent);
                startActivityForResult(intent,1);
                //Toast.makeText(TareaActivity.this, "Has pulsado: "+ tareas.get(position).getId_actividadarea(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private ArrayList<TareaDiaria> traeTareas(int id_ptt){

        Date anotherCurDate = new Date();
        SimpleDateFormat formatterDia = new SimpleDateFormat("dd-MM-yyyy");
        String fechaDia = formatterDia.format(anotherCurDate) + " 0:00:00";
        //String[] params = {fechaDia, String.valueOf(id_ptt)};
        String[] params = {String.valueOf(id_ptt)};

        ProgramacionDbHelper progDb = new ProgramacionDbHelper(TareaActivity.this);

        //Cursor c = progDb.traer("SELECT id_ptt, id_actividadarea, nombre_actividad, descripcion_actividad, id_area, id_actividad, tipo_tarea FROM Programacion_Diaria WHERE fecha_programacion = ? AND id_ptt = ? ORDER BY id_area, id_actividad", params);
        Cursor c = progDb.traer("SELECT id_ptt, id_actividadarea, nombre_actividad, descripcion_actividad, id_area, id_actividad, tipo_tarea FROM Programacion_Diaria WHERE id_ptt = ? ORDER BY id_area, id_actividad", params);

        //System.out.println("Cantidad Registros : "+c.getCount());
        ArrayList<TareaDiaria> tareas = new ArrayList<>();
        //ProgramacionDiaria prog;

        if (c.moveToFirst()) {
            do {
                TareaDiaria tar = new TareaDiaria();

                //System.out.println("Titulo Tarea : "+c.getString(1));

                tar.setId_ptt(c.getInt(0));
                tar.setId_actividadarea(c.getInt(1));
                tar.setNombre_actividad(c.getString(2));
                tar.setDescripcion_actividad(c.getString(3));
                tar.setId_area(c.getInt(4));
                tar.setId_actividad(c.getInt(5));
                tar.setTipo_tarea(c.getInt(6));

                RespuestasDbHelper respDb = new RespuestasDbHelper(TareaActivity.this);
                String[] par = {String.valueOf(c.getInt(0)), String.valueOf(c.getInt(1))};
                Cursor r = respDb.traer("SELECT estado_respuesta FROM Respuestas_Diaria WHERE id_ptt = ? AND id_actividadarea = ?", par);
                //System.out.println("Cantidad Registros Estado : "+r.getCount());
                if (r.moveToFirst()) {
                    do {
                        tar.setEstado_tarea(r.getInt(0));
                    } while (r.moveToNext());
                }else{
                    tar.setEstado_tarea(traeEstado("Pendiente"));
                }

                tareas.add(tar);
            }while(c.moveToNext());
        }

        return tareas;
    }

    public int traeEstado(String estado){

        System.out.println("*****************************************");
        System.out.println("Cant. Estados Tareas: "+Global.estadoTarea.length);
        for (int i = 0; i < Global.estadoTarea.length; i++) {
            System.out.println("Tarea: "+Global.estadoTarea[i].getNombre() +" --- "+estado);
            if (Global.estadoTarea[i].getNombre().equals(estado)) {
                System.out.println("estado: "+estado);
                System.out.println("estado tareas: "+Global.estadoTarea[i].getNombre());
                return Global.estadoTarea[i].getId();
            }
        }

        System.out.println("*****************************************");

        return -1;
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