package com.example.iniciodesesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class RegistroUsuario extends AppCompatActivity {

    // Declaración de variables utilies.
    Button btnAgregar;
    EditText user,pass,passConfirm,Correo;
    // Variable con la conexión a la DB.
    private FirebaseFirestore miCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        miCon = FirebaseFirestore.getInstance();

        this.InicializarControladores();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extraemos el texto ingresado.
                String u = user.getText().toString().trim();
                String p = pass.getText().toString().trim();
                String pC = passConfirm.getText().toString().trim();
                String cRR = Correo.getText().toString().trim();
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
                 if (u.isEmpty() || p.isEmpty() || pC.isEmpty() || cRR.isEmpty())
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
                else if (!pC.equals(p))
                {
                    Toast.makeText(getApplicationContext(),"Su contraseña ingresada no coincide.",Toast.LENGTH_LONG).show();
                } else {
                    EnviarDatos(u,pC,cRR);
                }
            }
        });
    }

    private void EnviarDatos(String u, String pC, String cRR)
    {
        Map <String, Object> map = new HashMap<>();
        map.put("User",u);
        map.put("Password",pC);
        map.put("Email",cRR);

        miCon.collection("Credenciales").document(u).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Datos ingresados exitosamente.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InicializarControladores()
    {
        user = findViewById(R.id.editUsuarioR);
        pass = findViewById(R.id.editContraR);
        passConfirm = findViewById(R.id.editContraConfirma);
        Correo = findViewById(R.id.editCorreoR);
        btnAgregar = findViewById(R.id.btnGuardarRegistro);
    }

    // Regresamos al lagin.
    public void Regresar(View vista)
    {
        Intent anterior = new Intent(this,MainActivity.class);
        startActivity(anterior);
    }
}