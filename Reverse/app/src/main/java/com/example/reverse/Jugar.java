package com.example.reverse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    private TextView fraseText, puntuacion;
    private EditText fraseUsuario;
    private Button botonEmpezar;
    private Button botonTerminar;
    private Button botonVolver;
    private Chronometer cronometro;
    private long time;

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
        botonEmpezar = findViewById(R.id.boton_jugar);
        botonTerminar = findViewById(R.id.boton_terminar);
        botonVolver = findViewById(R.id.boton_volver);
        cronometro = findViewById(R.id.cronometro);
        puntuacion = findViewById(R.id.puntuacion);



        //Sacamos los datos del intent
        Intent intent = getIntent();
        frase = (Frase) intent.getSerializableExtra("fraseJugar");
        fraseText.setText(frase.getFrase());

        botonTerminar.setVisibility(View.INVISIBLE);
        botonTerminar.setEnabled(false);

        botonEmpezar.setEnabled(true);
        botonTerminar.setVisibility(View.GONE);
        botonTerminar.setEnabled(false);
        botonVolver.setEnabled(true);

        botonEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fraseUsuario.setText("");
                cronometro.setBase(SystemClock.elapsedRealtime());
                time = 0;

                cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {

                        if (SystemClock.elapsedRealtime() - cronometro.getBase() >= 600000){
                            cronometro.setBase(SystemClock.elapsedRealtime());
                            //Toast aqui para informar al usuario
                            Toast.makeText(Jugar.this, "El tiempo ha superado el permitido, volviendo al inicio.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Jugar.this, HomeFragment.class);
                            startActivity(intent);
                        }
                    }
                });


                botonEmpezar.setEnabled(false);
                botonEmpezar.setVisibility(View.GONE);

                cronometro.start();
                botonTerminar.setVisibility(View.VISIBLE);
                botonTerminar.setEnabled(true);

                botonTerminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cronometro.stop();
                        time = SystemClock.elapsedRealtime() - cronometro.getBase();

                        botonVolver.setEnabled(true);
                        botonVolver.setVisibility(View.VISIBLE);

                        botonEmpezar.setEnabled(true);
                        botonEmpezar.setVisibility(View.VISIBLE);

                        String mensaje = "Reintentar";
                        botonEmpezar.setText(mensaje);

                        puntuacion.setText(puntuacionUsuario());
                        cronometro.setText(conversorTiempo());

                        //Reset del cronometro.

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

    private String conversorTiempo(){

        int minutos, segundos;

        segundos = (int) ((time-time%1000)/1000)%60;
        minutos = (int) ((((time-time%1000)/1000)-segundos)/60)%60;

        if (minutos < 10 && segundos < 10){
            return "0"+minutos+":0"+segundos;
        } else if (minutos < 10){
            return "0" + minutos + ":" + segundos;
        } else if (segundos < 10){
            return minutos+":0"+segundos;
        } else{
            return minutos+":"+segundos;
        }
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