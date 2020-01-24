package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.

 */
public class MapaAdministradorFragment extends Fragment  implements OnMapReadyCallback {
    public GoogleMap mGoogleMap;
    public MapView mMapView;
    public View mView;
    public DatabaseReference mDatabase;

    public MapaAdministradorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
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

        LatLng ubicacion_delito = new LatLng(ListaDenuncias.latitud_delito, ListaDenuncias.longitud_delito);

        mDatabase.child("Denuncia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion_delito));
        mGoogleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.guardia_actual)).position(ubicacion_delito));
        mGoogleMap.setMinZoomPreference(15);
    }
}
