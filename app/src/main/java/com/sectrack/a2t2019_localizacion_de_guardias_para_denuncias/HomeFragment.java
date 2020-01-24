package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;


import android.content.Intent;
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
    static public String tipo_delito;


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

        btnRobo.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            tipo_delito= btnRobo.getText().toString();
            getActivity().finish();
        });

        btnAgresion.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            tipo_delito= btnAgresion.getText().toString();
            getActivity().finish();
        });

        btnDrogas.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            tipo_delito= btnDrogas.getText().toString();
            getActivity().finish();
        });

        btnOtroDelito.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            getActivity().startActivity(new Intent(getActivity(), Reportar_Denuncia.class));
            tipo_delito= btnOtroDelito.getText().toString();
            getActivity().finish();
        });



        return view;
    }

}
