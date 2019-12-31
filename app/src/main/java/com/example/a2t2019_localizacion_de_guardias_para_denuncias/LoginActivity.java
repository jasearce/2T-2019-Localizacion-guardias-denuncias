package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public ImageView btnRegresar;
    public EditText userEditText;
    public EditText passEditText;
    public Button btnLogin;

    public FirebaseAuth mAuth;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        userEditText = findViewById(R.id.editTextEmail);
        passEditText = findViewById(R.id.editTextPass);

        btnRegresar = findViewById(R.id.imgRetornar);
        btnRegresar.setOnClickListener(v -> finish());

        btnLogin = findViewById(R.id.btnLoginActivity);
        btnLogin.setOnClickListener(v -> {
            String email = userEditText.getText().toString();
            String password = passEditText.getText().toString();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                userEditText.setError("Formato de Email invalido");
                userEditText.setFocusable(true);
            }
            else{
                login(email,password);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesion");

        checkConnection();
    }

    public void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(null != activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this, "Wifi: Encendido",Toast.LENGTH_SHORT).show();
            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "Datos moviles: Encendido",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No hay conexion a Internet",Toast.LENGTH_SHORT).show();
        }
    }

    private void login(String email, String password){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                progressDialog.dismiss();
                // Sign in success, update UI with the signed-in user's information
                FirebaseUser user = mAuth.getCurrentUser();
                startActivity(new Intent(LoginActivity.this, PerfilUsuarioActivity.class));
                finish();
            }else{
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error al iniciar sesion",Toast.LENGTH_SHORT).show());
    }
}
