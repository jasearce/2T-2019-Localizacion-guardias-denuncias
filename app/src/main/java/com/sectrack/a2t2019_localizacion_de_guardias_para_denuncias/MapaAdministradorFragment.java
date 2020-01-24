package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;

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
 * Este fragmento nos permitira visualizar todos los delitos reportados para el administrador
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

        mDatabase.child("Denuncia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot denunciaSnapshot : dataSnapshot.getChildren()) {
                    String estado =denunciaSnapshot.child("estado").getValue().toString();
                    String delito =denunciaSnapshot.child("tipo_delito").getValue().toString();
                    String latitud = denunciaSnapshot.child("latitud").getValue().toString();
                    String longitud = denunciaSnapshot.child("longitud").getValue().toString();
                    String descripcion= denunciaSnapshot.child("descripcion").getValue().toString();
                    Double latitudmap=Double.parseDouble(latitud);
                    Double longitudmap=Double.parseDouble(longitud);
                    LatLng delitomap = new LatLng(latitudmap,longitudmap);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(delitomap));
                    mGoogleMap.setMinZoomPreference(15);
                    if(estado.equalsIgnoreCase("NO")){
                        mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.exe)).title(delito).snippet(descripcion).position(delitomap));
                    }else{
                        mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.check)).title(delito).snippet(descripcion).position(delitomap));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });
    }
}
