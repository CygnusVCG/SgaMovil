package com.cygnus.sgamovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.cygnus.sgamovil.cls.IngresoLog;
import com.cygnus.sgamovil.cls.ParametrosUsuario;
import com.cygnus.sgamovil.cls.Usuarios;
import com.cygnus.sgamovil.sqlite.ParametrosUsuarioDbHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    static LocationManager mlocManager = null;
    static LocationListener mlocListener;
    private String versionName = "";

    SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        String userVar = myPreferences.getString("user", null);
        String passVar = myPreferences.getString("pass", null);

        EditText txtUsuario = (EditText)findViewById(R.id.editTextUsuario);
        EditText txtPassword = (EditText)findViewById(R.id.editTextPassword);

        if(userVar == null || passVar == null){
            txtUsuario.setText("");
            txtPassword.setText("");
        }else {
            txtUsuario.setText(userVar);
            txtPassword.setText(passVar);
        }

        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (Exception ex) {

        }
        TextView versionname = findViewById(R.id.vwVersion);
        versionname.setText("Cygnus SGA " + versionName);

        Button loginButton = (Button) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View view) {
                                               boolean valida = true;
                                               TextView txtUsuario = (EditText)findViewById(R.id.editTextUsuario);
                                               if (txtUsuario.length() == 0 ){
                                                   txtUsuario.setError( "El Usuario no puede estar en blanco" );
                                                   valida = false;
                                               }
                                               TextView txtPass = (EditText)findViewById(R.id.editTextPassword);
                                               if (txtPass.length() == 0 ){
                                                   txtPass.setError( "La contraseña no puede estar en blanco" );
                                                   valida = false;
                                               }

                                               if(valida){

                                                   SharedPreferences.Editor myEditor = myPreferences.edit();
                                                   myEditor.putString("user", txtUsuario.getText().toString());
                                                   myEditor.putString("pass", txtPass.getText().toString());
                                                   myEditor.commit();

                                                   try {
                                                       //VCG:20180418 Revisar no estoy seguro que lo este haciendo bien
                                                       //Globales.crearCarpetas();

                                                       String strWS = "http://services.cygnus-est.cl/wsRESTCYGNUS/api/usuario/?USR_Login=" + txtUsuario.getText() + "&USR_PASS=" + txtPass.getText() + "&SIS_Autoid=109";

                                                       //String strWS = "https://prod.cygnus.cl:9020/Usuario/validaUsuario?usuario=" + txtUsuario.getText() + "&pass=" + txtPass.getText() + "&sistema=109";

                                                       System.out.println(strWS);
                                                       final AsyncHttpClient client = new AsyncHttpClient();
                                                       final RequestHandle requestHandle = client.get(strWS, new AsyncHttpResponseHandler() {
                                                           @Override
                                                           public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                               if (statusCode == 200) {

                                                                   try {
                                                                       JSONObject json = new JSONObject(new String(responseBody));
                                                                       System.out.println(json);

                                                                       int session = json.getInt("Usr_autoid");

                                                                       if (session == 0) {
                                                                           AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                                                           builder1.setMessage("Usuario o Contraseña Incorrecta");
                                                                           builder1.setCancelable(true);

                                                                           builder1.setPositiveButton(
                                                                                   "Aceptar",
                                                                                   new DialogInterface.OnClickListener() {
                                                                                       public void onClick(DialogInterface dialog, int id) {
                                                                                           dialog.cancel();
                                                                                       }
                                                                                   });

                                                                           AlertDialog alert11 = builder1.create();
                                                                           alert11.show();

                                                                           // Log
                                                                           IngresoLog.generaLog(LoginActivity.this, Usuarios.usr_autoid, 1, "Login", "Usuario o contraseña incorrecta");

                                                                       } else {

                                                                           /*Usuarios.usr_autoid = json.getInt("usrUsuarioSession");
                                                                           Usuarios.uxs_autoid = json.getInt("uxsUsuarioSession");
                                                                           Usuarios.usr_nombre = json.getString("nombreUsuarioSession");
                                                                           Usuarios.usr_rut = json.getString("rutUsuarioSession");
                                                                           Usuarios.usr_mail = "";//json.getString("Usr_mail");
                                                                           Usuarios.token = json.getString("tokenSession");*/

                                                                           Usuarios.usr_autoid = json.getInt("Usr_autoid");
                                                                           Usuarios.usr_nombre = json.getString("Usr_nombre");
                                                                           Usuarios.usr_rut = json.getString("Usr_rut");
                                                                           Usuarios.usr_mail = json.getString("Usr_mail");

                                                                           /*// INI parametros usuario
                                                                           Usuarios.usr_folio = json.getString("Usr_folio");
                                                                           //Globales.setFolioNuevo( Integer.parseInt( Usuarios.usr_folio) + 1 );
                                                                           Usuarios.usr_monitoreo_mail = json.getString("Usr_monitoreo_mail");
                                                                           Usuarios.usr_monitoreo_gps = json.getString("Usr_monitoreo_gps");
                                                                           Usuarios.usr_toma_fotos = json.getString("Usr_toma_fotos");
                                                                           Usuarios.usr_version = json.getString("Usr_version");
                                                                           Usuarios.fecha_servidor = json.getString("Fecha_servidor");*/

                                                                           /*ParametrosUsuarioDbHelper puDb = new ParametrosUsuarioDbHelper(LoginActivity.this);
                                                                           ParametrosUsuario pu = new ParametrosUsuario();
                                                                           pu.usr_folio =  Usuarios.usr_folio;
                                                                           pu.usr_monitoreo_mail =  Usuarios.usr_monitoreo_mail;
                                                                           pu.usr_monitoreo_gps =  Usuarios.usr_monitoreo_gps;
                                                                           pu.usr_toma_fotos =  Usuarios.usr_toma_fotos;
                                                                           pu.usr_version =  Usuarios.usr_version;
                                                                           puDb.guardar(pu);*/

                                                                           // FIN parametros usuario

                                                                           // Log
                                                                           IngresoLog.generaLog(LoginActivity.this, Usuarios.usr_autoid, 1, "Login", "Validacion de Usuario en WS");

                                                                           Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                                                                           startActivity(intent);
                                                                       }
                                                                   } catch (JSONException e) {
                                                                       e.printStackTrace();
                                                                       // Log
                                                                       IngresoLog.generaLog(LoginActivity.this, Usuarios.usr_autoid, 2, "Login", e.toString());
                                                                   } catch (Exception e) {
                                                                       // Log
                                                                       IngresoLog.generaLog(LoginActivity.this, Usuarios.usr_autoid, 2, "Login", e.toString());
                                                                       e.printStackTrace();
                                                                   }
                                                               }
                                                           }

                                                           @Override
                                                           public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                           }
                                                       });
                                                   }catch (Exception ex){
                                                       // Log
                                                       IngresoLog.generaLog(LoginActivity.this, Usuarios.usr_autoid, 2, "Login", ex.toString());
                                                       ex.printStackTrace();
                                                   }
                                               } //---- END VALIDA
                                           }
                                       }
        );

        TextView recuperaPass = (TextView) findViewById(R.id.vwTxtOlvidaste);
        recuperaPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LinearLayout layoutRecupera = (LinearLayout) findViewById(R.id.lytRecupera);
                layoutRecupera.setVisibility(View.VISIBLE);
            }
        });

        ImageButton btnRecupera = (ImageButton) findViewById(R.id.btnRecupera);
        btnRecupera.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View view) {
                                               boolean valida = true;
                                               TextView txtUsuario = (EditText)findViewById(R.id.editTextUsuario);
                                               if (txtUsuario.length() == 0 ){
                                                   txtUsuario.setError( "El Usuario no puede estar en blanco" );
                                                   valida = false;
                                               }
                                               TextView txtMail = (EditText)findViewById(R.id.editMailRecupera);
                                               if (txtMail.length() == 0 ){
                                                   txtMail.setError( "El mail no puede estar en blanco" );
                                                   valida = false;
                                               }

                                               if(valida){
                                                   try {
                                                       String url = "http://services.cygnus-est.cl/wsRESTCYGNUS/api/usuario/?USR_login=" + txtUsuario.getText() + "&usr_mail=" + txtMail.getText();
                                                       final AsyncHttpClient client = new AsyncHttpClient();
                                                       final RequestHandle requestHandle = client.get(url, new AsyncHttpResponseHandler() {
                                                           @Override
                                                           public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                               if (statusCode == 200) {

                                                                   try {
                                                                       JSONObject json = new JSONObject(new String(responseBody));

                                                                       Usuarios.usr_autoid = json.getInt("Usr_autoid");
                                                                       Usuarios.usr_nombre = json.getString("Usr_nombre");
                                                                       Usuarios.usr_rut = json.getString("Usr_rut");
                                                                       Usuarios.usr_mail = json.getString("Usr_mail");

                                                                       AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                                                       builder1.setCancelable(true);
                                                                       builder1.setPositiveButton(
                                                                               "Aceptar",
                                                                               new DialogInterface.OnClickListener() {
                                                                                   public void onClick(DialogInterface dialog, int id) {
                                                                                       dialog.cancel();
                                                                                   }
                                                                               });

                                                                       if (Usuarios.usr_autoid == 0) {
                                                                           builder1.setMessage("Correo no registrado en nuestros sistemas");
                                                                       } else {
                                                                           builder1.setMessage("Se han enviado los datos de acceso al correo ingresado");
                                                                       }
                                                                       AlertDialog alert11 = builder1.create();
                                                                       alert11.show();

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
                                                       //System.out.println("Error : " + ex.getMessage());
                                                   }
                                               }
                                           }
                                       }
        );
        /*if(!isLocationEnabled(this)){
            alertaMsg("Encuestas SGM","Para usar esta aplicación debe estar activo el GPS");
            pantallaActivacionGPS();
        }*/
    }


    /*private void limpiarTablas(){
        PDSDbHelper pds = new PDSDbHelper(this);
        pds.limpiar();
    }*/


    //MÉTODO QUE ES UTILIZADO PARA VOLVER A ACTIVAR EL GPS CUANDO EL USUARIO LO DESHABILITA.
    private void pantallaActivacionGPS()
    {

        String provider = Settings.Secure.getString( this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps"))
        { //if gps is disabled

            try
            {
                Intent gpsHabilitar = new Intent("android.location.GPS_ENABLED_CHANGE");
                gpsHabilitar.putExtra("enabled", true);
                this.sendBroadcast(gpsHabilitar);
            }
            catch (Exception e)
            {
                // EL ANDROID QUE SE ESTA CONECTANDO NO TIENE EL INTENT "android.location.GPS_ENABLED_CHANGE" POR ESO SE CAE
            }

            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.sendBroadcast(poke);
        }
    }


    protected  void alertaMsg(String title, String mymessage)
    {
        new android.app.AlertDialog.Builder(LoginActivity.this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,	new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.cancel();

                    }
                }).show();
    }



    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}