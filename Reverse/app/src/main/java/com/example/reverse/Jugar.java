package com.example.reverse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

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
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView puntuacion;
    private Chronometer tiempo;
    private Button salir;
    private Button reintentar;

    private ArrayList<Object> frases;
    private Frase frase;
    private TinyDB tinyDB;

    private long PauseOffSet = 0;
    private boolean isPlaying = false;

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
            @Override
            public void onClick(View v) {
                //Reset del cronometro.
                PauseOffSet = 0;
                dialog.dismiss();
            }
        });
    }

    private int puntuacionUsuario (EditText freseUsuario){

        return 0;
    }






}