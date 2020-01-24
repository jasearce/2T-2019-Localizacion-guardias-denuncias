package com.sectrack.a2t2019_localizacion_de_guardias_para_denuncias;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
/**
 * A simple {@link Fragment} subclass.
 */

public class PerfilFragment extends Fragment {

    private ImageView avatarUser;
    private TextView txtID, txtNombre, txtApellidos, txtTelefono, txtEmail, txtCuenta;
    private Button btnLogout;


    public PerfilFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        /*Inicializamos la Firebase*/
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Usuarios");

        avatarUser = view.findViewById(R.id.profile_image);
        txtID = view.findViewById(R.id.txt_userID);
        txtNombre = view.findViewById(R.id.txt_nombre);
        txtApellidos = view.findViewById(R.id.txt_apellidos);
        txtEmail = view.findViewById(R.id.txt_correo);
        txtTelefono = view.findViewById(R.id.txt_telefonoUser);
        txtCuenta = view.findViewById(R.id.txt_tipo_cuenta);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            //SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            //Evitar regresar al Login con la flecha de retorno del celular
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        });

        Query query = reference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {




            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    /*Obtenemos la informacion*/
                    //String uid = "" + snapshot.child("UID").getValue();
                    String nombre = "" + snapshot.child("Nombre").getValue();
                    String apellidos = "" + snapshot.child("Apellidos").getValue();
                    String email = "" + snapshot.child("Email").getValue();
                    String telefono = "" + snapshot.child("Telefono").getValue();
                    String fotoPerfil = "" + snapshot.child("Imagen").getValue();
                    String tipoCuenta = "" + snapshot.child("Tipo de cuenta").getValue();
                    Uri urlfoto=Uri.parse(fotoPerfil);
                    if(!fotoPerfil.isEmpty()){
                        Glide.with(getActivity()).load(urlfoto).centerCrop().into(avatarUser);
                    }

                    /*Colocamos la informacion obtenida del Firebase en el View*/
                    String formato = nombre + " " + apellidos;
                    txtID.setText(formato);
                    txtNombre.setText(nombre);
                    txtApellidos.setText(apellidos);
                    txtEmail.setText(email);
                    txtTelefono.setText(telefono);
                    txtCuenta.setText("CLIENTE");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
