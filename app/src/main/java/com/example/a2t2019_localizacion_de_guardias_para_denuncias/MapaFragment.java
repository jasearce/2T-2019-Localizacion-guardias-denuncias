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
                /*Removemos los puntos del realTimeMarkers para evitar que se vuelvan a colocar los
                marcadores anteriores*/
                for(Marker marker:realTimeMarkers){
                    marker.remove();
                }
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Ubicacion ubi = snapshot.getValue(Ubicacion.class);
                    Double latitud = ubi.getLatitud();
                    Double longitud = ubi.getLongitud();

                    MarkerOptions markerOptions = new MarkerOptions();
                    agregarGuardias();
                    markerOptions.position(new LatLng(latitud, longitud));
                    tmpRealTimeMarkers.add(mGoogleMap.addMarker(markerOptions));

                    CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitud, longitud))
                            .zoom(15)
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

    private void agregarGuardias() {
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1438333392739195, -79.96212478609135)).title("EDCOM").snippet("Guardia en EDCOM").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1458741000000003,-79.94890597407598)).title("PARCOM").snippet("Guardia en PARCOM").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1502017,-79.9495475)).title("ADMISIONES").snippet("Guardia en ADMISIONES").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1520112253733545, -79.9533235538422)).title("Garita").snippet("Guardias en Garita").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1519958666914074,-79.95336846312456)).title("Biciletas Terminal").snippet("Guardias en Terminal de Biciletas").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.146772,-79.966155)).title("ASIRI").snippet("Guardia Parqueaderos Biblioteca central por ASIRI").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.148893,-79.967628)).title("CELEX").snippet("Guardia Parqueaderos CELEX parte de atras").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1514567091350516,-79.95454509409174)).title("CIBE").snippet("Guardia en CIBE").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1521011479322807,-79.95594879937659)).title("FCV").snippet("Guardia en FCV").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.148348477036179, -79.96371562917663)).title("RECTORADO").snippet("Guardia en RECTORADO").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.1468200881734965,-79.96302771313425)).title("MARITIMA").snippet("Guardia en MARITIMA").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.15237555, -79.95822722537349)).title("PISCINA").snippet("Guardia en PISCINA").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(-2.145925806984807, -79.9647382958111)).title("FICT").snippet("Guardia en parqueadero de FICT, frente al Coliseo").icon(BitmapDescriptorFactory.fromResource(R.drawable.police2)));







    }
}
