package com.cygnus.sgamovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cygnus.sgamovil.cls.Usuarios;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setNombreUsuario(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView txtViewUsuarioMenu = (TextView)headerView.findViewById(R.id.textViewUsuarioMenu);
        txtViewUsuarioMenu.setText(Usuarios.usr_nombre);
    }

    /*private void limpiarTablas(){
        PDSDbHelper pds = new PDSDbHelper(this);
        pds.limpiar();

    }*/

    @Override
    public void onBackPressed() {
        // VCG: 20180419 se comento para que no usaran el boton back
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prog_diaria) {
            Intent intent = new Intent( this, ProgramacionActivity.class);
            intent.putExtra("fechaProgramacion", "");
            startActivity(intent);
            finish();
        }if (id == R.id.nav_prog_anterior) {
            Intent intent = new Intent( this, ProgramacionAnteriorActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_pendientes) {
            Intent intent = new Intent( this, PendientesActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_add_tarea) {
            Intent intent = new Intent( this, AgregarActividadActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_envio_tarea) {
            Intent intent = new Intent( this, EnviarRespuestasActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_inc_reportadas) {
            Intent intent = new Intent( this, IncidenciasActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_ver_log) {
            Intent intent = new Intent( this, VerLogActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cerrar_session) {
            Intent intent = new Intent( this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
