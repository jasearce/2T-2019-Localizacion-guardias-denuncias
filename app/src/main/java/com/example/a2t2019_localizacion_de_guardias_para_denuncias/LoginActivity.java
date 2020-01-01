package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    public ImageView btnRegresar;
    public EditText userEditText;
    public EditText passEditText;
    public Button btnLogin;
    public SignInButton mGoogleSignInBtn;
    public GoogleSignInClient mGoogleSignInClient;

    public FirebaseAuth mAuth;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Parte de Google*/
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        /*Parte grafica*/
        userEditText = findViewById(R.id.editTextEmail);
        passEditText = findViewById(R.id.editTextPass);

        btnRegresar = findViewById(R.id.imgRetornar);
        btnRegresar.setOnClickListener(v -> finish());

        mGoogleSignInBtn = findViewById(R.id.btnGoogleSignIn);
        mGoogleSignInBtn.setSize(SignInButton.SIZE_STANDARD);

        mGoogleSignInBtn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, ""+user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, PerfilUsuarioActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Fallo en inicio de sesion",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,""+e.getMessage(),
                        Toast.LENGTH_SHORT).show());
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
                Toast.makeText(this, "Datos moviles: Encendido",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No hay conexion a Internet",
                    Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Por favor revise que sus credenciales sean correctas.",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, ""+e.getMessage(),
                Toast.LENGTH_SHORT).show());
    }
}
