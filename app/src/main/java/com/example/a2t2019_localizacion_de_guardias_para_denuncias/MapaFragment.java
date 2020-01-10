package com.example.a2t2019_localizacion_de_guardias_para_denuncias;


import android.app.Activity;
import android.app.Dialog;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
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
import java.util.List;


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

        mDatabase.child("Ubicacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*Removemos los puntos del realTimeMarkers para evitar que se vuelvan a colocar los
                marcadores anteriores*/
                for(Marker marker:realTimeMarkers){
                    marker.remove();
                }
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Ubicacion ubicacionCliente = snapshot.getValue(Ubicacion.class);
                    Double latitud = ubicacionCliente.getLatitud();
                    Double longitud = ubicacionCliente.getLongitud();

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud, longitud));
                    tmpRealTimeMarkers.add(mGoogleMap.addMarker(markerOptions));

                    CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitud, longitud))
                            .zoom(10)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*Registros del dispostivo IoT*/
        mDatabase.child("registro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String latFirebase = "" + snapshot.child("lat").getValue();
                    String lonFirebase = "" + snapshot.child("lon").getValue();
                    Ubicacion ubicacionGuardia = cambioALatLng(latFirebase, lonFirebase);
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(ubicacionGuardia.getLatitud(), ubicacionGuardia.getLongitud())))
                            .setTitle("Guardia");
                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(new LatLng(ubicacionGuardia.getLatitud(), ubicacionGuardia.getLongitud()))
                            .zoom(10)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toException());
            }
        });
    }

    private Ubicacion cambioALatLng(String latitud, String longitud){
        /*Cambio los string de Firebase hacia un tipo de dato Integer*/
        int latFirebaseInt = Integer.valueOf(latitud);
        int lonFirebaseInt = Integer.valueOf(longitud);

        /*Cambio los valores a hexadecimal*/
        String hexLatitud = Integer.toHexString(latFirebaseInt) + "00";
        String hexLongitud = Integer.toHexString(lonFirebaseInt) + "00";

        /*Cambio a un Long los valores hexadecimales*/
        Long lLatitud = Long.parseLong(hexLatitud,16);
        Long lLongitud = Long.parseLong(hexLongitud, 16);

        /*Finalmente obtengo los valores utiles de latitud y longitud*/
        float fLatitud= Float.intBitsToFloat(lLatitud.intValue());
        float fLongitud= Float.intBitsToFloat(lLongitud.intValue());

        /*Pasamos a tipo de datos double ya que para crear un LatLng se necesita dos double*/
        double dLatitud =(double) fLatitud;
        double dLongitud = (double) fLongitud;

        Ubicacion ubicacion = new Ubicacion(dLatitud, dLongitud);

        return ubicacion;
    }
}
