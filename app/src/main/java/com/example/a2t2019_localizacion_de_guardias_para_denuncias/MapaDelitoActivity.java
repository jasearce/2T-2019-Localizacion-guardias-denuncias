package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MapaDelitoActivity extends AppCompatActivity{
    ConstraintLayout descripcion_delito;
    String name, descripcion, delito, area, fecha, id, lati,longi;
    TextView namet, descripciont, delitot, fechat, areat;
    Button resolver_delito;
    DatabaseReference databaseD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_delito);
        databaseD= FirebaseDatabase.getInstance().getReference();
        descripcion_delito = (ConstraintLayout) findViewById(R.id.descripcion_delito);

        resolver_delito= (Button) findViewById(R.id.resolver_delito);

        name=getIntent().getExtras().getString("name");
        descripcion = getIntent().getExtras().getString("descripcion");
        delito= getIntent().getExtras().getString("delito");
        fecha=getIntent().getExtras().getString("fecha");
        area=getIntent().getExtras().getString("area");
        id=getIntent().getExtras().getString("id");
        lati= getIntent().getExtras().getString("lati");
        longi = getIntent().getExtras().getString("long");

        namet=(TextView)findViewById(R.id.nombre_txt);
        descripciont= (TextView)findViewById(R.id.descripcion_txt);
        delitot = (TextView)findViewById(R.id.delito_txt);
        fechat = (TextView)findViewById(R.id.fecha_txt);
        areat =(TextView)findViewById(R.id.area_txt);
        namet.setText(name);
        delitot.setText(delito);
        fechat.setText(fecha);
        areat.setText(area);
        descripciont.setText(descripcion);
        /*
        descripcion_delito.addView(delitot);
        descripcion_delito.addView(areat);
        descripcion_delito.addView(descripciont);
        descripcion_delito.addView(fechat);
        descripcion_delito.addView(namet);*/

        MapaDelitoFragment delitoFragment =new MapaDelitoFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.fragment_container_delito, delitoFragment,"");
        fragmentTransaction2.commit();


        resolver_delito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaDenuncias.class);

                databaseD.child("Denuncia").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot denunciaSnapshot : dataSnapshot.getChildren()) {
                            String idb = denunciaSnapshot.child("id").getValue().toString();
                            if(idb.equalsIgnoreCase(id)){
                                Denuncia denuncia=new Denuncia(id,delito,name,fecha,ListaDenuncias.latitud_delito,ListaDenuncias.longitud_delito,area,descripcion,"SI");
                                databaseD.child("Denuncia").child(id).setValue(denuncia);
                                Toast.makeText(getApplicationContext(), "Crimen Resuelto",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                    });
                startActivity(i);
            }
        });

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
