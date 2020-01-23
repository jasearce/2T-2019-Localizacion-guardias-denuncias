package com.example.a2t2019_localizacion_de_guardias_para_denuncias;


import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {
    public GoogleMap mGoogleMap;
    public MapView mMapView;
    public View mView;
    public DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();




    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_mapa, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.clear();

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);

        mDatabase.child("Ubicacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Removemos los puntos del realTimeMarkers para evitar que se vuelvan a colocar los
                //marcadores anteriores
                for(Marker marker:realTimeMarkers){
                    marker.remove();
                }
                //double latitud=DashboardActivity.lat;
                //double longitud=DashboardActivity.lon;

                double latitud1=0 ;
                double longitud1=0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Ubicacion ubi = new Ubicacion(Double.parseDouble(snapshot.child("latitud").getValue().toString()), Double.parseDouble(snapshot.child("longitud").getValue().toString()));
                    latitud1 = ubi.getLatitud();
                    longitud1 = ubi.getLongitud();
                }
                LatLng ultimo=new LatLng(latitud1,longitud1);

                CameraPosition cameraPosition = CameraPosition.builder().target(ultimo)
                        .zoom(18)
                        .bearing(0)
                        .tilt(45)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                agregarGuardias();
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Registros del dispostivo IoT/
        mDatabase.child("registro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ubicacion ubicacionGuardia = null;
                LatLng posicionGuardia ;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String latFirebase = "" + snapshot.child("lat").getValue();
                    String lonFirebase = "" + snapshot.child("lon").getValue();
                    ubicacionGuardia = cambioALatLng(latFirebase, lonFirebase);
                }
                posicionGuardia = new LatLng(ubicacionGuardia.getLatitud(), ubicacionGuardia.getLongitud());

                mGoogleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.guardia_actual)).position(posicionGuardia));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fallo en obtener datos de Firebase");
            }
        });
    }

    /**
     * Metodo agregarGuardias
     * Este metodo se encarga de agregar marcadores con su respectiva descripcion al mapa,
     * de esta manera podemos ver los guardias en ESPOL.
     */
    private void agregarGuardias() {
        mDatabase.child("GuardiasEspol").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String latFirebase = "" + snapshot.child("latitud").getValue();
                    String lonFirebase = "" + snapshot.child("longitud").getValue();
                    String descripcion = "" + snapshot.child("descripcion").getValue();
                    String ubicacion = "" + snapshot.child("ubicacion").getValue();
                    double latitud = Double.parseDouble(latFirebase);
                    double longitud = Double.parseDouble(lonFirebase);
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(ubicacion).snippet(descripcion).icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fallo en obtener datos de Firebase");
            }
        });
    }

    /**
     * Metooo cambioALatLng
     * Recibe los string obtenidos de la Firebase para convertirlos de su valor String
     * decimal hacia un valor de  double que se utilizara para colocar dentro del marker
     * de GoogleMaps.
     * @param latitud define la latitud a la cual se encuentra el guardia
     * @param longitud define la longiutd a la cual se encuentra el guardia
     * @return un objeto tipo Ubicacion que posee latitud y longitud
     */
    private Ubicacion cambioALatLng(String latitud, String longitud){
        Ubicacion ubicacion;
        double dLatitud;
        double dLongitud;

        if(latitud.equals("null") || longitud.equals("null")){
            //Posicion por defecto/
            dLatitud = -2.146772;
            dLongitud = -79.966155;
        }else {
            //Cambio los string de Firebase hacia un tipo de dato Integer/
            int latFirebaseInt = Integer.parseInt(latitud.trim());
            int lonFirebaseInt = Integer.parseInt(longitud.trim());

            //Cambio los valores a hexadecimal/
            String hexLatitud = Integer.toHexString(latFirebaseInt) + "00";
            String hexLongitud = Integer.toHexString(lonFirebaseInt) + "00";

            //Cambio a un Long los valores hexadecimales/
            Long lLatitud = Long.parseLong(hexLatitud,16);
            Long lLongitud = Long.parseLong(hexLongitud, 16);

            //Finalmente obtengo los valores utiles de latitud y longitud/
            float fLatitud= Float.intBitsToFloat(lLatitud.intValue());
            float fLongitud= Float.intBitsToFloat(lLongitud.intValue());

            //Pasamos a tipo de datos double ya que para crear un LatLng se necesita dos double/
            dLatitud =(double) fLatitud;
            dLongitud = (double) fLongitud;
        }
        ubicacion = new Ubicacion(dLatitud, dLongitud);
        return ubicacion;
    }
}
