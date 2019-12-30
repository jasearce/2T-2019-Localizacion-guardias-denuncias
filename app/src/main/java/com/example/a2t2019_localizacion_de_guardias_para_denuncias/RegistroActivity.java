package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    public EditText mEmailEt;
    public EditText mPassEt;
    public EditText mRepPassEt;
    public EditText mNombreEt;
    public EditText mApellidosEt;
    public EditText mTelefonoEt;
    public Button btnRegistro;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mEmailEt = findViewById(R.id.editTextEmail);
        mPassEt = findViewById(R.id.editTextPass);
        mRepPassEt = findViewById(R.id.editTextConfirmPass);
        mNombreEt = findViewById(R.id.editTextNombres);
        mApellidosEt = findViewById(R.id.editTextApellidos);
        mTelefonoEt = findViewById(R.id.editTextTelef);
        btnRegistro = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();

        btnRegistro.setOnClickListener(v -> {
            String name = mNombreEt.getText().toString();
            String apellidos = mApellidosEt.getText().toString();
            String email = mEmailEt.getText().toString();
            String password = mPassEt.getText().toString();
            String repPass = mRepPassEt.getText().toString();
            String telefono = mTelefonoEt.getText().toString();

            if(TextUtils.isEmpty(name)){
                Toast.makeText(RegistroActivity.this, "No ha ingresado datos en el campo",
                        Toast.LENGTH_SHORT).show();
                mNombreEt.setError("No existen datos en Nombre");
                mNombreEt.setFocusable(true);
            }else{
                mNombreEt.setError(null);
            }

            if(TextUtils.isEmpty(apellidos)){
                Toast.makeText(RegistroActivity.this, "No ha ingresado datos en el campo",
                        Toast.LENGTH_SHORT).show();
                mApellidosEt.setError("No existen datos en Apellidos");
                mApellidosEt.setFocusable(true);
            }else{
                mApellidosEt.setError(null);
            }

            if(TextUtils.isEmpty(email)){
                Toast.makeText(RegistroActivity.this, "No ha ingresado datos en el campo",
                        Toast.LENGTH_SHORT).show();
                mEmailEt.setError("No existen datos en Email");
                mEmailEt.setFocusable(true);
            }else{
                mEmailEt.setError(null);
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(RegistroActivity.this, "No ha ingresado datos en el campo",
                        Toast.LENGTH_SHORT).show();
                mPassEt.setError("No existen datos en Contraseña");
                mPassEt.setFocusable(true);
            }else{
                mPassEt.setError(null);
            }

            if(TextUtils.isEmpty(repPass)){
                Toast.makeText(RegistroActivity.this, "No ha ingresado datos en el campo",
                        Toast.LENGTH_SHORT).show();
                mRepPassEt.setError("No ha repetido su contraseña");
                mRepPassEt.setFocusable(true);
            }else{
                mRepPassEt.setError(null);
            }

            if(TextUtils.isEmpty(telefono)){
                Toast.makeText(RegistroActivity.this, "No ha ingresado datos en el campo",
                        Toast.LENGTH_SHORT).show();
                mTelefonoEt.setError("No existen datos en Telefono");
                mTelefonoEt.setFocusable(true);
            }else{
                mTelefonoEt.setError(null);
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
                mEmailEt.setError("Email Invalido!");
                Toast.makeText(RegistroActivity.this, "Formato incorrecto del email", Toast.LENGTH_SHORT).show();
                mEmailEt.setFocusable(true);
            }else{
                registrar(email,password);
            }
        });

    }
    private void registrar(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegistroActivity.this, "Registro exitoso",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistroActivity.this, PerfilUsuarioActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegistroActivity.this, "Autenticacion falló",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(RegistroActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}
