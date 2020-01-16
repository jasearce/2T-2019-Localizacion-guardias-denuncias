package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 100;
    public ImageView btnRegresar;
    public EditText userEditText;
    public EditText passEditText;
    public Button btnLogin;
    public SignInButton mGoogleSignInBtn;
    public GoogleSignInClient mGoogleSignInClient;
    private String token = null;

    public FirebaseAuth mAuth;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        /*Parte de Google*/
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this, "Por favor ingrese su usuario y contraseña",
                        Toast.LENGTH_SHORT).show();
                userEditText.setFocusable(true);
                passEditText.setFocusable(true);
            }
            else{
                userEditText.setError(null);
                passEditText.setError(null);
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
                if(account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        /*Si ingresa por primera vez el usuario se muestra us informacion de perfil de la cuenta de Google*/
                        if(task.getResult().getAdditionalUserInfo().isNewUser()){
                            //Obtengo el email y el id del usuario ingresado
                            String userEmail = user.getEmail();
                            String uid = user.getUid();
                            String nombres = user.getDisplayName();
                            String nombre=acct.getGivenName().toString().toUpperCase();
                            String apellido=acct.getFamilyName().toString().toUpperCase();
                            Uri urlFoto= acct.getPhotoUrl();
                            String imagen=urlFoto.toString();
                            System.out.println(nombres);

                            String telefono = user.getPhoneNumber();
                            //Guardo en un HashMap
                            HashMap<Object,String> hashMap = new HashMap<>();

                            hashMap.put("Email",userEmail);
                            hashMap.put("UID",uid);
                            hashMap.put("Nombre",nombre);
                            hashMap.put("Apellidos",apellido);
                            hashMap.put("Telefono",telefono);
                            hashMap.put("Imagen",imagen);

                            //Instancia de Firebase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Usuarios");
                            reference.child(uid).setValue(hashMap);
                        }
                        Toast.makeText(LoginActivity.this, ""+user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Fallo en inicio de sesion",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,""+e.getMessage(),
                        Toast.LENGTH_SHORT).show());
    }

    /**
     * Autor: Javier Arce
     * Metodo para verificar el tipo de conexion en el que se encuentra el dispositivo movil.
     * Ademas se muestra el mensaje que indica si no se posee conexion a internet.
     */

    public void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if(null != activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                //Toast.makeText(this, "Wifi: Encendido",Toast.LENGTH_SHORT).show();
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

    /**
     * Metodo login() permite iniciar sesion a un usuario ya registrado dentro de la base
     * de datos de Firebase. El metodo a traves de signInWithEmailAndPassword verifica la existencia
     * del usuario dentro de la base de datos.
     *
     * @param email ingresado dentro del screen de Login
     * @param password ingresado dentro del screen de Login
     */
    private void login(String email, String password){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                progressDialog.dismiss();
                FirebaseUser user = mAuth.getCurrentUser();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
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
