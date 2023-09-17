package com.cygnus.sgamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cygnus.sgamovil.cls.IngresoLog;
import com.cygnus.sgamovil.cls.EnvioRespuestas;
import com.cygnus.sgamovil.cls.ProgramacionDiaria;
import com.cygnus.sgamovil.cls.RespuestaActividad;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.ProgramacionDbHelper;
import com.cygnus.sgamovil.sqlite.RespuestasDbHelper;
import com.cygnus.sgamovil.sqlite.EnviosDbHelper;
import com.cygnus.sgamovil.cls.Envios;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;

public class EnvioActivity extends  AppCompatActivity
        implements View.OnClickListener {

    private ImageView imagenEstado;
    private Button btnCerrar;
    private TextView txtEstado, txtGuardados, txtEnviados;

    private int conteoRegistrosEnviar;
    private int conteoEnviadosCorrectos;
    private int conteoEnviadosError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio);

        imagenEstado = (ImageView) findViewById(R.id.imgEstado);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtGuardados = (TextView) findViewById(R.id.txtGuardados);
        txtEnviados = (TextView) findViewById(R.id.txtEnviados);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(this);

        btnCerrar.setEnabled(false);

        //Date anotherCurDate = new Date();
        //SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
        //String fechaDia = formatterDia.format(anotherCurDate);

        String fechaDia = getIntent().getExtras().getString("fecha");

        enviaDatos(fechaDia);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btnCerrar){
            this.finish();
        }
    }

    private void enviaDatos(String fecha){

        RespuestasDbHelper capDb = new RespuestasDbHelper(EnvioActivity.this);

        String[] params = {fecha, "0"};
        Cursor c = capDb.traer(" SELECT id_ptt, id_actividadarea, fecha_respuesta, hora_respuesta, estado_respuesta, conformidad_respuesta, rangorecepcion_respuesta, observacion_respuesta, foto_nombre_antes, foto_nombre_despues, ejecutor_actividad_respuesta, id_supervisor, _ID, id_area, id_actividad  FROM Respuestas_Diaria WHERE fecha_respuesta = ? AND estado_envio_respuesta = ?", params);

        conteoRegistrosEnviar = c.getCount();
        conteoEnviadosCorrectos = 0;
        conteoEnviadosError = 0;

        System.out.println("Conteo Respuestas: "+conteoRegistrosEnviar);

        EnvioRespuestas er = new EnvioRespuestas();
        RespuestaActividad[] resultSet = new RespuestaActividad[conteoRegistrosEnviar];
        int celda = 0;

        int idUsr = 0;
        int idRespuesta = 0;

        if (c.moveToFirst()) {
            do {

                RespuestaActividad resp = new RespuestaActividad();

                resp.setId_ptt(c.getInt(0));
                resp.setId_actividadarea(c.getInt(1));
                resp.setFecha_respuesta(c.getString(2));
                resp.setHora_respuesta(c.getString(3));
                resp.setEstado_respuesta(c.getInt(4));
                resp.setConformidad_respuesta(c.getInt(5));
                resp.setRangorecepcion_respuesta(c.getInt(6));
                resp.setObservacion_respuesta(c.getString(7));

                String fotoAntes = c.getString(8);
                resp.setFoto_nombre_antes(fotoAntes);
                String auxBaseAntes = "";
                if(fotoAntes.trim().length()>0)
                    auxBaseAntes = buscaFoto(c.getString(8));
                resp.setFoto_base_antes(auxBaseAntes);

                String fotoDespues = c.getString(9);
                resp.setFoto_nombre_despues(fotoDespues);
                String auxBaseDespues = "";
                if(fotoDespues.trim().length()>0)
                    auxBaseDespues = buscaFoto(c.getString(9));
                resp.setFoto_base_despues(auxBaseDespues);

                resp.setEjecutor_actividad_respuesta(c.getString(10));
                resp.setId_supervisor(c.getInt(11));

                idUsr = c.getInt(11);
                idRespuesta = c.getInt(12);

                resp.setId_area(c.getInt(13));
                resp.setId_actividad(c.getInt(14));

                enviarRespuestas(resp, idUsr, idRespuesta);

                //resultSet[celda] = resp;
                //celda++;

            }while(c.moveToNext());
        }
        c.close();

        // Log
        IngresoLog.generaLog(EnvioActivity.this, idUsr, 1, "feccantCapturas", fecha + " _ " +conteoRegistrosEnviar);

        //er.setRespuestas(resultSet);

        //enviarRespuestas(er,idUsr);
    }

    //private void enviarRespuestas(EnvioRespuestas c, int usuario){
    private void enviarRespuestas(RespuestaActividad c, int usuario, int respuesta){

        //String strWS = "http://services.cygnus-est.cl/wsREST_ENTELAPP/api/EntelVentasApp/VENTAS_INSERTAR/";

        String strWS = "https://b.sga.cygnus.cl/api/RespuestasMovil/Respuestas_insertar/";
        int TIMEOUT_WS = 200 * 10000;

        final AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(TIMEOUT_WS);
        client.setMaxRetriesAndTimeout(1, TIMEOUT_WS);
        StringEntity entity;

        /*String strWS = "https://b.sga.cygnus.cl/Movil/IngresaRespuestasDiarias?"+
                        "id_actividad=" + c.id_actividad +
                        "&fecha_respuesta=" + c.fecha_respuesta +
                        "&hora_respuesta=" + c.hora_respuesta +
                        "&id_ptt=" + c.id_ptt +
                        "&id_actividadarea=" + c.id_actividadarea +
                        "&estado_respuesta=" + c.estado_respuesta +
                        "&conformidad_respuesta=" + c.conformidad_respuesta +
                        "&rangorecepcion_respuesta=" + c.rangorecepcion_respuesta +
                        "&observacion_respuesta=" + c.observacion_respuesta +
                        "&foto_antes=" + c.id_ptt +"_"+ c.foto_antes +
                        "&foto_despues=" + c.id_ptt +"_"+ c.foto_despues +
                        "&ejecutor_actividad_respuesta=" + c.ejecutor_actividad_respuesta +
                        "&id_supervisor=" + c.id_supervisor;*/

        System.out.println(strWS);
        //final AsyncHttpClient client = new AsyncHttpClient();
        final int usr = usuario;

        try{
            Gson gson = new Gson();
            String json = gson.toJson(c);
            entity = new StringEntity(json , "UTF-8");

            System.out.println(entity);
            System.out.println(c);
            System.out.println(json);

            writeToFile(json, EnvioActivity.this);

            // Log
            IngresoLog.generaLog(EnvioActivity.this, usr, 1, "jsonRespuestas", c.toString());

            client.addHeader("Content-type", "application/json;charset=utf-8");
            final RequestHandle requestHandle = client.post( EnvioActivity.this, strWS,entity, "application/json;" , new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, @NonNull byte[] responseBody) {
                    System.out.println(statusCode);
                    String resultado = new String(responseBody);
                    System.out.println(resultado);

                    // Log
                    IngresoLog.generaLog(EnvioActivity.this, usr, 1, "enviarRespuestas", "Resultado Envio = "+statusCode);



                    if (statusCode == 200) {
                        System.out.println("======");
                        System.out.println(resultado);
                        System.out.println("======");

                        int res = Integer.parseInt(resultado);
                        actualizaEnvio(res,respuesta);
                        conteoEnviadosCorrectos++;
                    }else{
                        conteoEnviadosError++;
                    }

                    if(conteoRegistrosEnviar == (conteoEnviadosCorrectos + conteoEnviadosError)){
                        registraEnvio(conteoEnviadosCorrectos, conteoEnviadosError);
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, @NonNull Throwable error) {
                    String resultado = new String(responseBody);

                    imagenEstado.setImageResource(R.drawable.fail);
                    txtEstado.setText("Falla Envio ("+statusCode+") : "+resultado);
                    btnCerrar.setEnabled(true);
                    //System.out.println(resultado);
                    // Log
                    IngresoLog.generaLog(EnvioActivity.this, usr, 2, "fallaRespuestas", "Falla Envio ("+statusCode+") : "+resultado);
                }
            });

        }catch (Exception ex){
            imagenEstado.setImageResource(R.drawable.fail);
            txtEstado.setText(ex.getMessage());
            btnCerrar.setEnabled(true);

            // Log
            IngresoLog.generaLog(EnvioActivity.this, usr, 2, "fallaRespuestas", ex.toString());
        }
    }

    public void actualizaEnvio(int resultado, int respuesta){
        if(resultado > 0) {
            Date anotherCurDate = new Date();
            SimpleDateFormat formatterDiaHora = new SimpleDateFormat("d/M/yyyy hh:mm");
            String formatoDateString = formatterDiaHora.format(anotherCurDate);

            SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
            String fechaEnvio = formatterDia.format(anotherCurDate);

            final Calendar cal = Calendar.getInstance();
            int mMonth = cal.get(Calendar.MONTH);

            //String strQry = "UPDATE Respuestas_Diaria SET estado_envio_respuesta = 1 WHERE estado_envio_respuesta = 0 AND fecha_respuesta = "+fechaEnvio;
            String strQry = "UPDATE Respuestas_Diaria SET estado_envio_respuesta = 1 , id_respuesta_server = " + resultado  + " WHERE _ID = "+respuesta;
            System.out.println(strQry);
            RespuestasDbHelper respDb = new RespuestasDbHelper(EnvioActivity.this);
            respDb.actualizar(strQry);

            // Log
            //IngresoLog.generaLog(EnvioActivity.this, 0, 2, "registraEnvio", "Se actualizo "+resultado+" respuestas");
        }else{
            IngresoLog.generaLog(EnvioActivity.this, 0, 2, "registraEnvio", "respuesta " + respuesta + " no registro en servidor");

        }
    }

    public void registraEnvio(int correctos, int incorrectos){
        System.out.println("Correctos: "+correctos);
        System.out.println("Incorrectos: "+incorrectos);

        if(correctos > 0) {
            Date anotherCurDate = new Date();
            SimpleDateFormat formatterDiaHora = new SimpleDateFormat("d/M/yyyy hh:mm");
            String formatoDateString = formatterDiaHora.format(anotherCurDate);

            SimpleDateFormat formatterDia = new SimpleDateFormat("yyyy-MM-dd");
            String fechaEnvio = formatterDia.format(anotherCurDate);

            final Calendar cal = Calendar.getInstance();
            int mMonth = cal.get(Calendar.MONTH);

            // Log
            IngresoLog.generaLog(EnvioActivity.this, 0, 2, "registraEnvio", "Se actualizaron "+correctos+" respuestas");

            EnviosDbHelper envioDb = new EnviosDbHelper(EnvioActivity.this);
            Envios envio = new Envios();
            envio.setFecha_envio(fechaEnvio);
            envio.setMes_envio(mMonth);
            envio.setRegs_enviados(conteoRegistrosEnviar);
            envio.setRegs_grabados(correctos);
            envio.setFecha_hora_enviado(formatoDateString);
            envio.setTipoEnvio("");
            envio.setEstado_envio(1);

            long aux = envioDb.guardar(envio);

            // Log
            IngresoLog.generaLog(EnvioActivity.this, 0, 2, "registraEnvio", "Se registro el envio ("+aux+"), Registros guardados = " + correctos + " - Registros erroneos = " + incorrectos + " - Registros enviados = " + conteoRegistrosEnviar);

            imagenEstado.setImageResource(R.drawable.ok);
            txtEstado.setText("El envio de Respuestas fue realizado! ");
            txtGuardados.setText("Registros correctos : " + correctos);
            txtGuardados.setVisibility(View.VISIBLE);
            //txtGuardados.setText("Registros incorrectos : " + incorrectos);
            //txtGuardados.setVisibility(View.VISIBLE);
            txtEnviados.setText("Registros enviados : " + conteoRegistrosEnviar);
            txtEnviados.setVisibility(View.VISIBLE);
            //btnEnviar.setBackgroundResource(R.drawable.btn_enviar);
            btnCerrar.setEnabled(true);

        }else{
            //Toast.makeText(this, "No se registro correctamente el envio, intentar nuevamente", Toast.LENGTH_LONG).show();
            imagenEstado.setImageResource(R.drawable.fail);
            txtEstado.setText("No se realizo correctamente el envio, intentar nuevamente !");
            txtGuardados.setText("Registros incorrectos : " + incorrectos);
            txtGuardados.setVisibility(View.VISIBLE);
            //btnEnviar.setBackgroundResource(R.drawable.btn_enviar);
            btnCerrar.setEnabled(true);

            // Log
            IngresoLog.generaLog(EnvioActivity.this, 0, 2, "registraEnvio", "No se registro el envio ("+incorrectos+")");

        }
    }

    public String buscaFoto(String foto){
        boolean encontro = false;
        String foto64 = "";
        try{
            //File f = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/" + foto);
            File f = new File(foto);
            byte[] bytesArray = new byte[(int) f.length()];
            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray);
            fis.close();
            //foto.setFoto_base64(Base64.encodeToString(bytesArray, Base64.DEFAULT)); //Base64.NO_WRAP + Base64.URL_SAFE
            foto64 = Base64.encodeToString(bytesArray, Base64.DEFAULT); //Base64.NO_WRAP + Base64.URL_SAFE
            encontro = true;
            System.out.println("------------------- Encontro "+foto+"-------------------------");
            //System.out.println(foto64);
        }catch (Exception ex){
            System.out.println("---- Excepcion ----");
            System.out.println(ex.getMessage());
        }

        return foto64;
    }

    private void writeToFile(@NonNull String data, @NonNull Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}