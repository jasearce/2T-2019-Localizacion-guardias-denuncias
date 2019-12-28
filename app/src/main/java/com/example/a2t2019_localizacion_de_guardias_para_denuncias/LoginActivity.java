package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private ImageView btnRegresar;
    private EditText userEditText;
    private EditText passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegresar = findViewById(R.id.imgRetornar);
        btnRegresar.setOnClickListener(v -> finish());
    }
}
