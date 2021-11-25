package com.example.reverse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reverse.ui.home.HomeFragment;

import java.util.ArrayList;

public class Jugar extends AppCompatActivity{

    private TextView fraseText;
    private EditText fraseUsuario;
    private Button boton;
    private Button botonVolver;
    private Chronometer cronometro;
    private long time;

    //PopuUp

    private ArrayList<Object> frases;
    private Frase frase;
    private TinyDB tinyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        tinyDB = new TinyDB(this);

        fraseText = findViewById(R.id.frase);
        fraseUsuario = findViewById(R.id.usuario_frase);
        boton = findViewById(R.id.boton_jugar);
        botonVolver = findViewById(R.id.boton_volver);
        cronometro = findViewById(R.id.cronometro);



        //Sacamos los datos del intent
        Intent intent = getIntent();
        frase = (Frase) intent.getSerializableExtra("fraseJugar");
        fraseText.setText(frase.getFrase());

        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.setFormat("MM:SS");
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if (SystemClock.elapsedRealtime() - cronometro.getBase() >= 600000){
                    cronometro.setBase(SystemClock.elapsedRealtime());
                    //Toast aqui para informar al usuario
                    Toast.makeText(Jugar.this, "El tiempo ha superado el permitido", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Jugar.this, HomeFragment.class);
                    startActivity(intent);
                }
            }
        });

        boton.setText("Empezar");

        boton.setEnabled(true);
        botonVolver.setEnabled(true);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cronometro.start();

                boton.setText("Acabar");

                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cronometro.stop();
                        time = SystemClock.elapsedRealtime() - cronometro.getBase();
                        boton.setEnabled(false);
                        botonVolver.setEnabled(false);
                        contactPopUp();
                    }
                });
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Jugar.this, HomeFragment.class);
                startActivity(intent);
            }
        });
    }

    private void contactPopUp(){

        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        TextView puntuacion;
        TextView tiempo;
        Button salir;
        Button reintentar;


        //Meter la informacion del cronometro aqui

        puntuacion = findViewById(R.id.puntuacion_popup);
        tiempo = findViewById(R.id.tiempo_popup);
        reintentar = findViewById(R.id.reintentar_popup);
        salir = findViewById(R.id.salir_popup);
        puntuacion.setText(puntuacionUsuario());
        tiempo.setText(conversorTiempo());

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopUp = getLayoutInflater().inflate(R.layout.popup_jugar, null);

        dialogBuilder.setView(contactPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Asignación de la puntuacion y tiempo a la frase (Se prioriza una puntuacion alta al tiempo)
                if (Integer.parseInt(puntuacion.getText().toString()) > frase.getPuntuacion()){

                    frase.setPuntuacion(Integer.parseInt(puntuacion.getText().toString()));
                    frase.setTiempo(time);
                    frases.add(frase);
                    tinyDB.putListObject("frases", frases);

                } else if ((Integer.parseInt(puntuacion.getText().toString()) == frase.getPuntuacion()) && (time < frase.getTiempo())){

                    frase.setTiempo(time);
                    frases.add(frase);
                    tinyDB.putListObject("frases", frases);
                }

                Intent intent = new Intent(Jugar.this, HomeFragment.class);
                startActivity(intent);

                dialog.dismiss();

            }
        });

        reintentar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //Reset del cronometro.
                time = 0;
                boton.setText("Empezar");

                dialog.dismiss();
            }
        });
    }

    private String conversorTiempo(){

        int minutos, segundos;

        segundos = (int) ((time-time%1000)/1000)%60;
        minutos = (int) ((((time-time%1000)/1000)-segundos)/60)%60;

        return minutos+":"+segundos;
    }

    private int puntuacionUsuario (){

        int puntos = frase.getPuntuacionMaxima();
        int aux;

        if (fraseUsuario.getText().toString().equals(frase.getFraseInvertida())){

            return frase.getPuntuacionMaxima();
        }
        else if (fraseUsuario.length() == frase.getFraseInvertida().length()){

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
        else {

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