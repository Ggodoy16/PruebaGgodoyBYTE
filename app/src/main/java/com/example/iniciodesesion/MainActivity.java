package com.example.iniciodesesion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentReference;


public class MainActivity extends AppCompatActivity {

    EditText userI, passI;
    Button btnIniciarSesion;
    FirebaseFirestore miCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.InicializarControladores();
        miCon = FirebaseFirestore.getInstance();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extraemos el texto ingresado.
                String u = userI.getText().toString().trim();
                String p = passI.getText().toString().trim();
                int letraM = 0;

                // Validamos si la contraseña tiene una mayuscula.
                String regex = "^(?=.*[A-Z]).{7,}$";
                boolean isNumeric = (p != null && p.matches(regex));

                if (isNumeric) {
                    letraM = 1;
                } else {
                    letraM = 0;
                }

                // Validamos que los datos coincidan con las condiciones de aceptacion.
                if (u.isEmpty() || p.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"No ha ingresado todos los datos.",Toast.LENGTH_LONG).show();
                }else if (letraM == 0){
                    Toast.makeText(getApplicationContext(), "La contraseña debe llevar una letra mayuscula y ser mayor a 6 caracteres.", Toast.LENGTH_LONG).show();
                }else if (p.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "La contraseña debe ser mayor a 6 caracteres.", Toast.LENGTH_LONG).show();
                }
                else if (u.length() < 8)
                {
                    Toast.makeText(getApplicationContext(), "Usuario debe ser mayor a 8 caracteres.", Toast.LENGTH_SHORT).show();
                }
                else {
                    ValidarUsuario(u,p);
                }
            }
        });
    }

    private void ValidarUsuario(String us, String ps)
    {
        miCon.collection("Credenciales").document(us).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userDB = documentSnapshot.get("User").toString();
                String passDB = documentSnapshot.get("Password").toString();

                if (us.trim().equals(userDB.trim()) && ps.trim().equals(passDB.trim()))
                {
                    Intent bienvenido = new Intent(getApplicationContext(),Bienvenido.class);
                    bienvenido.putExtra("nombreUsuario", userDB);
                    startActivity(bienvenido);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), userDB+"-"+passDB, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Los datos son incorrectos.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void InicializarControladores() {
        userI = findViewById(R.id.editUsuario);
        passI = findViewById(R.id.editTextTextPassword);
        btnIniciarSesion = findViewById(R.id.btningresar);
    }


    // Cambio de pantalla para registrar.
    public void RegistrarUsuario(View vista)
    {
        Intent siguiente = new Intent(this,RegistroUsuario.class);
        startActivity(siguiente);
    }
}