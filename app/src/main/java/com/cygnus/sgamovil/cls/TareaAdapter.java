package com.cygnus.sgamovil.cls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cygnus.sgamovil.R;
import com.cygnus.sgamovil.TareaActivity;

import java.util.ArrayList;

public class TareaAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<TareaDiaria> tareas;

    public TareaAdapter(Context context, int layout, ArrayList<TareaDiaria> tar) {
        this.context = context;
        this.layout = layout;
        this.tareas = tar;
    }

    @Override
    public int getCount() {
        return this.tareas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tareas.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // Copiamos la vista
        View v = convertView;

        //Inflamos la vista con nuestro propio layout
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        System.out.println("Position : "+position);

        v = layoutInflater.inflate(R.layout.item_tarea, null);
        // Valor actual según la posición

        TareaDiaria actualTarea = tareas.get(position);

        String nombreArea = traeArea(actualTarea.getId_area());

        // Referenciamos el elemento a modificar y lo rellenamos
        TextView tarTitulo = (TextView) v.findViewById(R.id.tarea_titulo);
        tarTitulo.setText(actualTarea.getNombre_actividad()+" ("+nombreArea+")");

        TextView tarDescripcion = (TextView) v.findViewById(R.id.tarea_descripcion);
        tarDescripcion.setText(actualTarea.getDescripcion_actividad());

        String nombreEstado = traeEstado(actualTarea.getEstado_tarea());
        System.out.println("$$$$$$$$$$$$$$$$$$$$");
        System.out.println(actualTarea.getEstado_tarea());

        TextView tarEstado = (TextView) v.findViewById(R.id.tarea_estado);
        tarEstado.setText(nombreEstado);
        if(actualTarea.getEstado_tarea() == 9){
            tarEstado.setTextColor(Color.parseColor("#0ca401"));
        }else{
            if(actualTarea.getEstado_tarea() == 10){
                tarEstado.setTextColor(Color.parseColor("#FBDA00"));
            }else{
                tarEstado.setTextColor(Color.parseColor("#CC5223"));
            }
        }

        TextView tarAreaActividad = (TextView) v.findViewById(R.id.areaactividad_id);
        tarAreaActividad.setText(actualTarea.getId_ptt()+"-"+actualTarea.getId_actividadarea());

        //Devolvemos la vista inflada
        return v;
    }

    public String traeArea(int id){

        for (int i = 0; i < Global.areas.length; i++) {
            if (Global.areas[i].getId() == id) {
                return Global.areas[i].getNombre();
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