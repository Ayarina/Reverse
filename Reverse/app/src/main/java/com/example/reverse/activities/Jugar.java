package com.example.reverse.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reverse.R;
import com.example.reverse.models.Resultado;
import com.example.reverse.ui.home.HomeFragment;
import com.example.reverse.models.Frase;
import com.example.reverse.adapter.FraseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Jugar extends AppCompatActivity{

    private TextView fraseText, puntuacion;
    private EditText fraseUsuario;
    private Button botonEmpezar;
    private Button botonTerminar;
    private Button botonSalir;
    private Chronometer cronometro;
    private long time;

    private ArrayList<Object> frases;
    private Frase frase;
    private Resultado result;


    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance("https://reverse-f3fee-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        frase = (Frase) intent.getSerializableExtra("fraseJugar");

        myRef.child("Frases").child(frase.getFrase()).child("Usuarios").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    result = new Resultado();
                    Log.d("Jugar", "Result not found");

                } else {
                    result = task.getResult().getValue(Resultado.class);
                    if(result == null) {
                        result = new Resultado();
                        Log.d("Jugar", "Result null asignado a vacío");
                    }
                    else{
                        Log.d("Jugar", "Result asignado a: score=" + result.getPuntuacion() + " time=" + result.getTiempo());
                    }

                }
            }
        });


        fraseText = findViewById(R.id.frase);
        fraseUsuario = findViewById(R.id.usuario_frase);
        botonEmpezar = findViewById(R.id.boton_jugar);
        botonTerminar = findViewById(R.id.boton_terminar);
        botonSalir = findViewById(R.id.boton_salir);
        cronometro = findViewById(R.id.cronometro);
        puntuacion = findViewById(R.id.puntuacion);

        fraseText.setText(frase.getFrase());

        botonTerminar.setVisibility(View.INVISIBLE);
        botonTerminar.setEnabled(false);

        botonEmpezar.setEnabled(true);
        botonSalir.setEnabled(true);

        botonEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fraseUsuario.setText("");
                cronometro.setBase(SystemClock.elapsedRealtime());
                time = 0;

                botonEmpezar.setVisibility(View.INVISIBLE);
                botonEmpezar.setEnabled(false);

                botonTerminar.setVisibility(View.VISIBLE);
                botonTerminar.setEnabled(true);

                cronometro.start();

                cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {

                        if (SystemClock.elapsedRealtime() - cronometro.getBase() >= 600000){
                            cronometro.setBase(SystemClock.elapsedRealtime());
                            //Toast aqui para informar al usuario
                            Toast.makeText(Jugar.this, "El tiempo ha superado el permitido, volviendo al inicio.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        botonTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cronometro.stop();
                time = SystemClock.elapsedRealtime() - cronometro.getBase();

                String mensaje = "Reintentar";
                botonEmpezar.setText(mensaje);

                botonEmpezar.setEnabled(true);
                botonEmpezar.setVisibility(View.VISIBLE);

                botonTerminar.setVisibility(View.INVISIBLE);
                botonTerminar.setEnabled(false);

                puntuacion.setText(String.valueOf(puntuacionUsuario()));

                if (!fraseUsuario.getText().toString().isEmpty()) {

                    if (Integer.parseInt(puntuacion.getText().toString()) > result.getPuntuacion()) {
                        Log.d("Jugar", "puntuacion mayor");
                        result.setTiempo(time);
                        result.setPuntuacion(Integer.parseInt(puntuacion.getText().toString()));

                    } else if ((Integer.parseInt(puntuacion.getText().toString()) == result.getPuntuacion()) && (time < result.getTiempo())) {
                        Log.d("Jugar", "Mejor tiempo");
                        result.setTiempo(time);
                    }

                    myRef.child("Frases").child(frase.getFrase()).child("Usuarios").child(user.getUid()).setValue(result);

                }
            }
        });

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private int puntuacionUsuario (){

        int puntos = frase.getPuntuacionMaxima();
        int aux;

        if (fraseUsuario.length() == frase.getFraseInvertida().length()){

            for (int i = 0; i < fraseUsuario.length(); i++){

                if (fraseUsuario.toString().charAt(i) != frase.getFraseInvertida().charAt(i)){
                    puntos -= 2;
                }
            }
        }
        else if (fraseUsuario.length() < frase.getFraseInvertida().length()){

            for (aux = 0; aux < fraseUsuario.length(); aux++){

                if (fraseUsuario.toString().charAt(aux) != frase.getFraseInvertida().charAt(aux)){
                    puntos -= 2;
                }
            }

            aux = frase.getFraseInvertida().length() - (aux + 1);
            puntos -= (aux * 2);
        }
        else if (fraseUsuario.length() > frase.getFraseInvertida().length()){

            for (aux = 0; aux < frase.getFraseInvertida().length(); aux++){

                if (fraseUsuario.toString().charAt(aux) != frase.getFraseInvertida().charAt(aux)){
                    puntos -= 2;
                }
            }

            aux = fraseUsuario.length() - (aux + 1);
            puntos -= (aux * 2);
        }

        return puntos;
    }
}