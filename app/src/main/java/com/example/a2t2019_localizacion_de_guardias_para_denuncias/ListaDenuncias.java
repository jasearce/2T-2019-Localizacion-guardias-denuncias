package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;

public class ListaDenuncias extends AppCompatActivity {
    LinearLayout contenedorResultado;
    DatabaseReference databaseD;
    public static double latitud_delito;
    public static double longitud_delito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_denuncias);

        databaseD= FirebaseDatabase.getInstance().getReference();
        contenedorResultado = (LinearLayout)findViewById(R.id.linealBusqueda);

        databaseD.child("Denuncia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot denunciaSnapshot : dataSnapshot.getChildren()) {
                    String estado = denunciaSnapshot.child("estado").getValue().toString();
                    if(estado.equalsIgnoreCase("NO")) {
                        String id = denunciaSnapshot.child("id").getValue().toString();
                        String delito = denunciaSnapshot.child("tipo_delito").getValue().toString();
                        String name = denunciaSnapshot.child("nombre").getValue().toString();
                        String fecha = denunciaSnapshot.child("fecha").getValue().toString();
                        String latitud_real = denunciaSnapshot.child("latitud").getValue().toString();
                        String longitud_real = denunciaSnapshot.child("longitud").getValue().toString();
                        String area = denunciaSnapshot.child("area").getValue().toString();
                        String descripcion = denunciaSnapshot.child("descripcion").getValue().toString();

                        latitud_delito = Double.parseDouble(latitud_real);
                        longitud_delito = Double.parseDouble(longitud_real);


                        TextView nameTextView = new TextView(getApplicationContext());

                        nameTextView.setText(delito + " " + fecha + " " + area);
                        nameTextView.setClickable(true);
                        nameTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), MapaDelitoActivity.class);
                                i.putExtra("name", name);
                                i.putExtra("delito", delito);
                                i.putExtra("descripcion", descripcion);
                                i.putExtra("fecha", fecha);
                                i.putExtra("area", area);
                                i.putExtra("id", id);
                                i.putExtra("lati", latitud_real);
                                i.putExtra("long", longitud_real);

                                startActivity(i);

                            }
                        });
                        nameTextView.setTextColor(Color.BLACK);

                        nameTextView.setTextSize(20);
                        contenedorResultado.addView(nameTextView);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
