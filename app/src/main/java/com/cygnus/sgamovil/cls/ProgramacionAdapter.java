package com.cygnus.sgamovil.cls;

import android.content.Context;
import android.graphics.Color;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cygnus.sgamovil.R;

import java.util.ArrayList;

public class ProgramacionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ProgramacionDiaria> programaciones;

    public ProgramacionAdapter(Context context, int layout, ArrayList<ProgramacionDiaria> progs) {
        this.context = context;
        this.layout = layout;
        this.programaciones = progs;
    }

    @Override
    public int getCount() {
        return this.programaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return this.programaciones.get(position);
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

        v = layoutInflater.inflate(R.layout.item_programacion, null);
        // Valor actual según la posición

        System.out.println("Position : "+position);

        ProgramacionDiaria actualProg = programaciones.get(position);

        String horario = actualProg.getHora_inicio() + '-' + actualProg.getHora_termino();

        // Referenciamos el elemento a modificar y lo rellenamos
        TextView prgTitulo = (TextView) v.findViewById(R.id.prog_titulo);
        prgTitulo.setText(actualProg.getTitulo_tarea());

        //TextView prgDescripcion = (TextView) v.findViewById(R.id.prog_descripcion);
        //prgDescripcion.setText(actualProg.getDescripcion_tarea());

        TextView prgEjecucion = (TextView) v.findViewById(R.id.prog_Ejecucion);
        prgEjecucion.setText(actualProg.getDescripcion_ejecucion());

        TextView prgObservacion = (TextView) v.findViewById(R.id.prog_Observacion);
        if(actualProg.getObservacion_tarea().trim().length()>0) {
            prgObservacion.setText(actualProg.getObservacion_tarea());
        }else{
            prgObservacion.setText("Sin Observaciones");
        }

        TextView prgHorario = (TextView) v.findViewById(R.id.prog_Horario);
        prgHorario.setText(horario);

        TextView prgId = (TextView) v.findViewById(R.id.prog_id);
        prgId.setText(""+actualProg.getId_programacion());
        //Devolvemos la vista inflada
        return v;
    }
}