package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Reportar_Denuncia extends AppCompatActivity {

    private Spinner spinner1;
    private EditText descrip_txt;
    private TextView area_txt,description_txt;
    private Button denunciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar__denuncia);

        descrip_txt= (EditText)findViewById(R.id.edit_descrip);
        spinner1=(Spinner)findViewById(R.id.spinner);
        area_txt=(TextView)findViewById(R.id.area);
        description_txt=(TextView)findViewById(R.id.description);
        denunciar=(Button)findViewById(R.id.denunciar_bttn);

        String[] opciones={"FIEC 24A", "FIEC 15 A", "FIMCP 24C","FCSH BLOQUE 32", "FCV"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner1.setAdapter(adapter);

    }

    //Método del botón
    public void Denunciar(View view){
        String descripcion_String= description_txt.getText().toString();

        String seleccion=spinner1.getSelectedItem().toString();

        Toast.makeText(this,"Denuncia realizada con exito",Toast.LENGTH_LONG).show();
    }

}