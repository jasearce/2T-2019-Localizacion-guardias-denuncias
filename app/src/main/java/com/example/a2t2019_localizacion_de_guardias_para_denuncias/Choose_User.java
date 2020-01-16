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

        //user_bttn.setOnClickListener(v -> startActivity(new Intent(Choose_User.this,LoginActivity.class)));
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.user_bttn:
                intent = new Intent(Choose_User.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.guardian_bttn:
                intent = new Intent(Choose_User.this, LoginGuardiaActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.admin_bttn:
                intent = new Intent(Choose_User.this, LoginAdminActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}