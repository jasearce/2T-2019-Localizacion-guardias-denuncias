package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Reportar_Denuncia extends AppCompatActivity {
    private Spinner spinner1;
    public EditText descrip_txt;
    public TextView area_txt,description_txt,denunciante_name;
    public Button denunciar;
    double latitud;
    double longitud;


    DatabaseReference databaseDenuncia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar__denuncia);


        /*Inicializamos la Firebase*/
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Usuarios");


        databaseDenuncia= FirebaseDatabase.getInstance().getReference("Denuncia");

        descrip_txt = findViewById(R.id.edit_descrip);
        spinner1 = findViewById(R.id.spinner);
        area_txt = findViewById(R.id.area);
        description_txt = findViewById(R.id.description);
        denunciar = findViewById(R.id.denunciar_bttn);
        denunciante_name=findViewById(R.id.denunciante_name);



        String[] opciones={"FIEC 24A","FIEC 16A", "FIEC 15 A", "FCSH 32A","FCSH 32B","FCSH 32C", "FIMCP 24C", "FCV","ADMISIONES","RETORADO","FCNM 31AB","CELEX","CTI"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner1.setAdapter(adapter);




        Query query = reference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    /*Obtenemos la informacion*/
                    String nombre = "" + snapshot.child("Nombre").getValue();
                    String apellidos = "" + snapshot.child("Apellidos").getValue();


                    /*Colocamos la informacion obtenida del Firebase en el View*/
                    String name = nombre + " " + apellidos;
                    denunciante_name.setText(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }







    //Método del botón
    public void Denunciar(View view){
        if(!checkConnection()){
            Toast.makeText(this, "No hay conexion a Internet",
                    Toast.LENGTH_SHORT).show();
        }else{
            String name= denunciante_name.getText().toString();
            String descripcion_String= descrip_txt.getText().toString();
            String area=spinner1.getSelectedItem().toString();


            //Calendar c = Calendar.getInstance();
            Date date=new Date();
            int month= date.getMonth()+1;
            int day= date.getDay()+19;
            int year=1900+date.getYear();
            int hour=date.getHours()-5;
            int min=date.getMinutes();
            String date_String= String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year)+" Hora: "+ String.valueOf(hour)+":"+String.valueOf(min);

            if(!TextUtils.isEmpty(descripcion_String)){
                String id=databaseDenuncia.push().getKey();

                Denuncia denuncia=new Denuncia(id,HomeFragment.tipo_delito,name,date_String,latitud,longitud,area,descripcion_String);

                databaseDenuncia.child(id).setValue(denuncia);
                Toast.makeText(this,"Denuncia realizada con exito",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(this,     DashboardActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Debes describir el crimen",Toast.LENGTH_LONG).show();
            }



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