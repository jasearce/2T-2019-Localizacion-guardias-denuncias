package com.example.a2t2019_localizacion_de_guardias_para_denuncias;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    public BottomNavigationView bottomNavigationView;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient fusedLocationClient;
    public DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*Instanciacion de variables a usar en el Activity*/
        firebaseAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        subirLatLngFirebase();

        /*Vista por defecto del fragment*/
        MapaFragment mapaFragment = new MapaFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.fragment_container, mapaFragment,"");
        fragmentTransaction2.commit();
        checkConnection();
    }

    /**
     * Metodo: subirLatLngFirebase
     * Autor: Javier Arce
     * El objetivo del metodo es enviar a la base de datos de Firebase la ubicacion propia del celular
     * mediante el FusedLocationClient. Ademas se solicita el permiso para la app acceda a la ubicacion del
     * propio dispositivo.
     */
    private void subirLatLngFirebase() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.e("Latitud: ",+location.getLatitude()+"Longitud: "+location.getLongitude());
                        HashMap<String,Object> latLng = new HashMap<>();
                        latLng.put("latitud",location.getLatitude());
                        latLng.put("longitud",location.getLongitude());
                        databaseReference.child("Ubicacion").push().setValue(latLng);
                    }
                });
    }
    /**
     * Autor: Javier Arce
     * Mediante el onNavigationItemSelected se permitira el desplazamiento entre los fragment
     * que se generan en el BottomNavigationView, esto mediante una sentencia switch-case.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = menuItem -> {
        /*Aqui se maneja la seleccion de las opciones del menu*/
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                //Fragment de la pagina principal
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, homeFragment,"");
                fragmentTransaction.commit();
                return true;

            case R.id.nav_map:
                //Fragment de la pagina de mapas
                MapaFragment mapaFragment = new MapaFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container, mapaFragment,"");
                fragmentTransaction2.commit();
                return true;

            case R.id.nav_profile:
                //Fragment de la pagina de perfil de usuario
                PerfilFragment perfilFragment = new PerfilFragment();
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.fragment_container, perfilFragment,"");
                fragmentTransaction3.commit();
                return true;
        }
        return false;
    };

    /**
     * Metodo: checkUserStatus
     * Autor: Javier Arce
     * El objetivo del metodo es verificar el estado en el que se encuentra el usuario una vez ingresado
     * o cerrado sesion mediante Firebase; es decir, si ha iniciado sesion con exito por lo tanto el usuario
     * puede usar la app, caso contrario se lo regresa a la actividad de inicio de sesion
     */
    private void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //Usuario que ha iniciado sesion se mantiene aqui
            Toast.makeText(this,"Estoy en el Dashboard",Toast.LENGTH_SHORT).show();
        }else{
            //Usuario se lo regresa a que inicie sesion
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Autor: Javier Arce
     * Metodo para verificar el tipo de conexion en el que se encuentra el dispositivo movil.
     * Ademas se muestra el mensaje que indica si no se posee conexion a internet.
     */
    public void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(null != activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this, "Wifi: Encendido",Toast.LENGTH_SHORT).show();
            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "Datos moviles: Encendido",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No hay conexion a Internet",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        //Chequeamos el status
        checkUserStatus();
        super.onStart();
    }
}
