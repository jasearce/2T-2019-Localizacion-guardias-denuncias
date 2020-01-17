package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Choose_Registro extends AppCompatActivity {

    public ImageButton user_bttn, guardian_bttn,admin_bttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__registro);

        user_bttn = findViewById(R.id.user_bttn);
        guardian_bttn = findViewById(R.id.guardian_bttn);
        admin_bttn = findViewById(R.id.admin_bttn);
    }
    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.user_bttn:
                intent = new Intent(Choose_Registro.this, RegistroActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.guardian_bttn:
                intent = new Intent(Choose_Registro.this, Registro_Guardia.class);
                startActivity(intent);
                finish();
                break;
            case R.id.admin_bttn:
                intent = new Intent(Choose_Registro.this, Registra_Admin.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
