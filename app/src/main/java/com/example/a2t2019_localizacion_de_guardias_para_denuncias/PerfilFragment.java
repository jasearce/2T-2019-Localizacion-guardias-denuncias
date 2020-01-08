package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    public ImageView avatarUser;
    public TextView txtID, txtNombre, txtApellidos, txtTelefono, txtEmail, txtCuenta;


    public PerfilFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        /*Inicializamos la Firebase*/
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Usuarios");

        avatarUser = view.findViewById(R.id.imv_foto);
        txtID = view.findViewById(R.id.txt_userID);
        txtNombre = view.findViewById(R.id.txt_nombre);
        txtApellidos = view.findViewById(R.id.txt_apellidos);
        txtEmail = view.findViewById(R.id.txt_correo);
        txtTelefono = view.findViewById(R.id.txt_telefonoUser);
        txtCuenta = view.findViewById(R.id.txt_tipo_cuenta);

        Query query = reference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    /*Obtenemos la informacion*/
                    String uid = "" + snapshot.child("UID").getValue();
                    String nombre = "" + snapshot.child("Nombre").getValue();
                    String apellidos = "" + snapshot.child("Apellidos").getValue();
                    String email = "" + snapshot.child("Email").getValue();
                    String telefono = "" + snapshot.child("Telefono").getValue();
                    String fotoPerfil = "" + snapshot.child("Imagen").getValue();


                    /*Colocamos la informacion obtenida del Firebase en el View*/
                    txtID.setText(uid);
                    txtNombre.setText(nombre);
                    txtApellidos.setText(apellidos);
                    txtEmail.setText(email);
                    txtTelefono.setText(telefono);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
