package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Choose_User extends AppCompatActivity {

    public ImageButton user_bttn, guardian_bttn,admin_bttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__user);

        user_bttn = findViewById(R.id.user_bttn);
        guardian_bttn = findViewById(R.id.guardian_bttn);
        admin_bttn = findViewById(R.id.admin_bttn);
    }

    /**
     * Metodo onClick
     * Permite el cambio de contexto entre pantalla; ademas que permite enviar el tipo de cuenta por el cual
     * se va a realizar el inicio de sesion
     * @param view conexion entre parte grafica y logica
     */
    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.user_bttn:
                intent = new Intent(Choose_User.this, LoginActivity.class);
                intent.putExtra("tipo de cuenta", "Cliente");   //pasamos el tipo de informacion al nodo de usuarios
                startActivity(intent);
                finish();
                break;
            case R.id.guardian_bttn:
                intent = new Intent(Choose_User.this, LoginGuardiaActivity.class);
                intent.putExtra("tipo de cuenta", "Guardia");
                startActivity(intent);
                finish();
                break;
            case R.id.admin_bttn:
                intent = new Intent(Choose_User.this, LoginAdminActivity.class);
                intent.putExtra("tipo de cuenta", "Administrador");
                startActivity(intent);
                finish();
                break;
        }
    }
}