package com.example.a2t2019_localizacion_de_guardias_para_denuncias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    public Button btnLogin;
    public TextView txtRegistrarse;
    private VideoView videoBackground;
    public MediaPlayer mMediaPlayer;
    int currentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancio el videoview para manejar acciones del video
        videoBackground = findViewById(R.id.videoBack);
        //Crear uri para pasarlo al VideoView
        String pattern = "android.resource://"+ getPackageName() + "/" + R.raw.video607;
        Uri uri = Uri.parse(pattern);
        //Pasamos el uri al videoView y lo empezamos
        videoBackground.setVideoURI(uri);
        videoBackground.start();
        //Manejo del video player
        videoBackground.setOnPreparedListener(mediaPlayer -> {
            mMediaPlayer = mediaPlayer;
            //Se hace el loop para que reproduzca una y otra vez
                    mMediaPlayer.setLooping(true);
            if(currentVideoPosition != 0){
                mMediaPlayer.seekTo(currentVideoPosition);
                mMediaPlayer.start();
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        txtRegistrarse = findViewById(R.id.txtRegistro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //En caso de pausar la actividad, el video tambien lo hara
        currentVideoPosition = mMediaPlayer.getCurrentPosition(); //Obtengo la posicion de donde se esta reproduciendo el video
        //Pausamos el video
        videoBackground.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Reiniciamos el video en caso de volver a la actividad
        videoBackground.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Dejamos de lado nuestro video y lo seteamos a null
        mMediaPlayer.release();
        mMediaPlayer = null;

    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btnLogin:
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.txtRegistro:
                intent = new Intent(this, RegistroActivity.class);
                startActivity(intent);
                break;
        }
    }
}
