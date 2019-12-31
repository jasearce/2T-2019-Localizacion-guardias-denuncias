package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilUsuarioActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //Usuario que ha iniciado sesion se mantiene aqui
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
