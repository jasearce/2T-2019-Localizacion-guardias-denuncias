package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Reportar_Denuncia extends AppCompatActivity {

    private Spinner spinner1;
    public EditText descrip_txt;
    public TextView area_txt,description_txt;
    public Button denunciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar__denuncia);

        descrip_txt = findViewById(R.id.edit_descrip);
        spinner1 = findViewById(R.id.spinner);
        area_txt = findViewById(R.id.area);
        description_txt = findViewById(R.id.description);
        denunciar = findViewById(R.id.denunciar_bttn);

        String[] opciones={"FIEC 24A", "FIEC 15 A", "FIMCP 24C","FCSH BLOQUE 32", "FCV"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner1.setAdapter(adapter);

    }

    //Método del botón
    public void Denunciar(View view){
        if(!checkConnection()){
            Toast.makeText(this, "No hay conexion a Internet",
                    Toast.LENGTH_SHORT).show();
        }else{
            String descripcion_String= description_txt.getText().toString();
            String seleccion=spinner1.getSelectedItem().toString();
            Toast.makeText(this,"Denuncia realizada con exito",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Autor: Javier Arce
     * Metodo para verificar el tipo de conexion en el que se encuentra el dispositivo movil.
     * Ademas se muestra el mensaje que indica si no se posee conexion a internet.
     */
    public boolean checkConnection(){
        boolean verificar = true;

        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if(null != activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this, "Wifi: Encendido",Toast.LENGTH_SHORT).show();
            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "Datos moviles: Encendido",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            verificar = false;
        }

        return verificar;
    }



}