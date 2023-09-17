package com.cygnus.sgamovil.cls;

import android.content.Context;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cygnus.sgamovil.R;

import java.util.ArrayList;

public class IncidenciasAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<IncidenciasDiarias> incidencias;

    public IncidenciasAdapter(Context context, int layout, ArrayList<IncidenciasDiarias> inc) {
        this.context = context;
        this.layout = layout;
        this.incidencias = inc;
    }

    @Override
    public int getCount() {
        return this.incidencias.size();
    }

    @Override
    public Object getItem(int position) {
        return this.incidencias.get(position);
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

        v = layoutInflater.inflate(R.layout.item_incidencia, null);
        // Valor actual según la posición

        System.out.println("Position : "+position);

        IncidenciasDiarias actualInci = incidencias.get(position);

        String informante = actualInci.getNombre_persona() + ' ' + actualInci.getPaterno_personasempresa();

        // Referenciamos el elemento a modificar y lo rellenamos
        TextView prgTitulo = (TextView) v.findViewById(R.id.area_titulo);
        prgTitulo.setText(actualInci.getNombre_area());

        TextView prgDescripcion = (TextView) v.findViewById(R.id.tarea_descripcion);
        prgDescripcion.setText(actualInci.getComentario());

        TextView prgHorario = (TextView) v.findViewById(R.id.txtInformanteIncidencia);
        prgHorario.setText(informante);

        TextView prgId = (TextView) v.findViewById(R.id.id_incidencia);
        prgId.setText(""+actualInci.getId_incidencia());
        //Devolvemos la vista inflada
        return v;
    }
}