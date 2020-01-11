package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Button btnLogin;
    public TextView txtRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        txtRegistrarse = findViewById(R.id.txtRegistro);
    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btnLogin:
                intent = new Intent(this,Choose_User.class);
                startActivity(intent);
                break;
            case R.id.txtRegistro:
                intent = new Intent(this, RegistroActivity.class);
                startActivity(intent);
                break;
        }
    }
}
