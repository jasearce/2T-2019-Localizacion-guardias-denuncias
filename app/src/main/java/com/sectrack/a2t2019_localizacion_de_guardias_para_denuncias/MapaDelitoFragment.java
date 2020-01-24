package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;


import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Este fragmento me permitira visualizar un delito en especifico en el mapa para que el guardia acuda al lugar
 */
public class MapaDelitoFragment extends Fragment implements OnMapReadyCallback {
    public GoogleMap mGoogleMap;
    public MapView mMapView;
    public View mView;
    public DatabaseReference mDatabase;

    public MapaDelitoFragment() {
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
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion_delito));
        mGoogleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.exe)).position(ubicacion_delito));
        mGoogleMap.setMinZoomPreference(15);
    }
}
