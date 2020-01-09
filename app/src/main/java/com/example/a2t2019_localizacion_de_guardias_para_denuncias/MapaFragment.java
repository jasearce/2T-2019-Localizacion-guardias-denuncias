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
                    Ubicacion ubi = snapshot.getValue(Ubicacion.class);
                    Double latitud = ubi.getLatitud();
                    Double longitud = ubi.getLongitud();

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
        /*
        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502))
                .title("Estatua de la Libertad"));
        CameraPosition liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
        */
    }
}
