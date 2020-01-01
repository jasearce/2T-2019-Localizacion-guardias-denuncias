package com.example.a2t2019_localizacion_de_guardias_para_denuncias;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilUsuarioActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    public Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        btnCerrarSesion = findViewById(R.id.btnLogout);

        firebaseAuth = FirebaseAuth.getInstance();

        btnCerrarSesion.setOnClickListener(v -> {
            firebaseAuth.signOut();
            checkUserStatus();
        });
    }

    private void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //Usuario que ha iniciado sesion se mantiene aqui
            Toast.makeText(this,"Estoy en perfil usuario",Toast.LENGTH_SHORT).show();
        }else{
            //Usuario se lo regresa a que inicie sesion
            startActivity(new Intent(PerfilUsuarioActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        //Chequeamos el status
        checkUserStatus();
        super.onStart();
    }

}
