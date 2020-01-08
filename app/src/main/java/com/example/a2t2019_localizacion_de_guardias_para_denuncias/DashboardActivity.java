package com.example.a2t2019_localizacion_de_guardias_para_denuncias;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        /*Vista por defecto del fragment*/
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeFragment,"");
        fragmentTransaction.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            /*Aqui se maneja la seleccion de las opciones del menu*/
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    //Fragment de la pagina principal
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, homeFragment,"");
                    fragmentTransaction.commit();
                    return true;

                case R.id.nav_map:
                    //Fragment de la pagina de mapas
                    MapaFragment mapaFragment = new MapaFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_container, mapaFragment,"");
                    fragmentTransaction2.commit();
                    return true;

                case R.id.nav_profile:
                    //Fragment de la pagina de perfil de usuario
                    PerfilFragment perfilFragment = new PerfilFragment();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment_container, perfilFragment,"");
                    fragmentTransaction3.commit();
                    return true;
            }


            return false;
        }
    };

    private void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //Usuario que ha iniciado sesion se mantiene aqui
            Toast.makeText(this,"Estoy en el Dashboard",Toast.LENGTH_SHORT).show();
        }else{
            //Usuario se lo regresa a que inicie sesion
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        //Chequeamos el status
        checkUserStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Obtenemos el ID del item del menu
        int id = item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();

        }
        return super.onOptionsItemSelected(item);
    }
}
