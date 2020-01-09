package com.example.a2t2019_localizacion_de_guardias_para_denuncias;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button btnRobo, btnAgresion, btnDrogas, btnOtroDelito;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnRobo = view.findViewById(R.id.btnDenunciaRobo);
        btnAgresion = view.findViewById(R.id.btnDenunciaAgresion);
        btnDrogas = view.findViewById(R.id.btnDenunciaDrogas);
        btnOtroDelito = view.findViewById(R.id.btnDenunciaOtro);

        return view;
    }

}
