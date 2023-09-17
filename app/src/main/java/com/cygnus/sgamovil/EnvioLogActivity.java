package com.cygnus.sgamovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cygnus.sgamovil.cls.EnvioLogs;
import com.cygnus.sgamovil.cls.IngresoLog;
import com.cygnus.sgamovil.cls.Log;
import com.cygnus.sgamovil.sqlite.LogDbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import com.google.gson.Gson;

public class EnvioLogActivity extends  AppCompatActivity//BaseActivity
        implements View.OnClickListener {

    private ImageView imagenEstado;
    private Button btnCerrar;
    private TextView txtEstado;
    private String fecha;

    private int conteoRegistrosEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_log);

        imagenEstado = (ImageView) findViewById(R.id.imgEstado);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(this);

        btnCerrar.setEnabled(false);

        fecha = (String)getIntent().getExtras().get("fecha");

        enviaDatos();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btnCerrar){
            this.finish();
        }
    }

    private void enviaDatos(){

        LogDbHelper logDb = new LogDbHelper(EnvioLogActivity.this);

        String[] params = {fecha,"0"};

        Cursor c = logDb.traer(" SELECT _ID, usr_log, hora_log, tipo_log, accion_log, mensaje_log  FROM acciones WHERE fecha_log = ? and envio_log = ?", params);

        conteoRegistrosEnviar = c.getCount();

        EnvioLogs el = new EnvioLogs();
        Log[] resultSet = new Log[conteoRegistrosEnviar];
        int celda = 0;

        int idUsr = 0;

        if (c.moveToFirst()) {
            do {

                Log lg = new Log();

                lg.setUsuarioLog(Integer.parseInt(c.getString(1)));
                lg.setFechaLog(fecha);
                lg.setHoraLog(c.getString(2));
                lg.setTipoLog(Integer.parseInt(c.getString(3)));
                lg.setAccionLog(c.getString(4));
                lg.setMensajeLog(c.getString(5));

                resultSet[celda] = lg;
                celda++;

            }while(c.moveToNext());
        }
        c.close();

        el.setLogs(resultSet);

        enviarLogs(el,idUsr);
    }

    private void enviarLogs(EnvioLogs c, int usuario){

        String strWS = "http://services.cygnus-est.cl/wsRESTCYGNUS/api/IMEILOG/logs_insertar/";
        //System.out.println(strWS);
        final AsyncHttpClient client = new AsyncHttpClient();
        final int usr = usuario;

        try{
            Gson gson = new Gson();
            String json = gson.toJson(c);

            System.out.println(json);
            StringEntity entity = new StringEntity(json);

            System.out.println(entity.toString());

            final RequestHandle requestHandle = client.post( EnvioLogActivity.this, strWS, entity, "application/json" , new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    //System.out.println(statusCode);
                    String resultado = new String(responseBody);
                    //System.out.println(resultado);

                    registraEnvioLog(statusCode, Integer.parseInt(resultado));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String resultado = new String(responseBody);
                    //System.out.println(resultado);
                }
            });
        }catch (Exception ex){
            imagenEstado.setImageResource(R.drawable.fail);
            txtEstado.setText(ex.getMessage());
            btnCerrar.setEnabled(true);

            // Log
            IngresoLog.generaLog(EnvioLogActivity.this, usr, 2, "enviarLogs", ex.toString());
        }
    }

    public void registraEnvioLog(int status, int cant){

        if(status == 200){
            LogDbHelper logDb = new LogDbHelper(EnvioLogActivity.this);
            ContentValues actRegistro = new ContentValues();
            actRegistro.put("envio_log", 1);

            String[] params = {"0",fecha};

            int cantActualizadosInt = logDb.actualizacion(actRegistro, params);

            // Log
            IngresoLog.generaLog(EnvioLogActivity.this, 0, 2, "registraEnvioLog", "Se enviaron "+conteoRegistrosEnviar+" registros de log -- Se guardaron "+cant+" registros de log -- Se act. internamente "+cantActualizadosInt+" registros de log");

            imagenEstado.setImageResource(R.drawable.ok);
            txtEstado.setText("El envio de LOGS fue realizado correctamente. \r\n  Registros guardados = " + cant + " \r\n  Registros enviados = " + conteoRegistrosEnviar+ " \r\n  Registros act. Interno = " + cantActualizadosInt);
            btnCerrar.setEnabled(true);
        }else{
            //Toast.makeText(this, "No se registro correctamente el envio, intentar nuevamente", Toast.LENGTH_LONG).show();
            imagenEstado.setImageResource(R.drawable.fail);
            txtEstado.setText("No se realizo correctamente el envio, intentar nuevamente");
            //btnEnviar.setBackgroundResource(R.drawable.btn_enviar);
            btnCerrar.setEnabled(true);

            // Log
            IngresoLog.generaLog(EnvioLogActivity.this, 0, 2, "registraEnvioLog", "No se registro el envio de Log ("+status+")");

        }
    }

}