package com.example.iniciodesesion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Bienvenido extends AppCompatActivity {

    TextView nomUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);
        nomUser = findViewById(R.id.txtBienvenidoUser);


        Intent intent = getIntent();
        if (intent != null) {
            String textoRecibido = intent.getStringExtra("nombreUsuario");

            if (textoRecibido != null) {
                nomUser.setText(textoRecibido);
            }

        }

    }
}
