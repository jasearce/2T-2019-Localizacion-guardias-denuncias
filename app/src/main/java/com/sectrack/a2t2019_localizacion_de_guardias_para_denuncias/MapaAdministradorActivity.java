package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

public class MapaAdministradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_administrador);

        MapaAdministradorFragment administradorFragment =new MapaAdministradorFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.fragment_container_administrador, administradorFragment,"");
        fragmentTransaction2.commit();
        checkConnection();
    }

    /**
     * Autor: Javier Arce
     * Metodo para verificar el tipo de conexion en el que se encuentra el dispositivo movil.
     * Ademas se muestra el mensaje que indica si no se posee conexion a internet.
     */

    public void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
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

}
