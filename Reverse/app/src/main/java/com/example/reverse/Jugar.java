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

public class Jugar extends AppCompatActivity {

    private TextView fraseText;
    private EditText fraseUsuario;
    private Chronometer cronometro;
    private Button fin;
    private Button empezar;

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
        cronometro = findViewById(R.id.cronometro);
        empezar = findViewById(R.id.empezar);
        fin = findViewById(R.id.fin);

        fin.setVisibility(View.GONE);

        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometro.setBase(SystemClock.elapsedRealtime() - PauseOffSet);
                cronometro.start();
                isPlaying = true;

                fin.setVisibility(View.VISIBLE);

                fin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactPopUp(fraseUsuario);
                    }
                });
            }
        });
    }

    private void contactPopUp(EditText fraseUsuario){

        puntuacion = findViewById(R.id.puntuacion_popup);
        tiempo = findViewById(R.id.tiempo_popup);
        reintentar = findViewById(R.id.reintentar_popup);
        salir = findViewById(R.id.salir_popup);

        puntuacion.setText(puntuacionUsuario(fraseUsuario));
        tiempo.setBase(cronometro.getBase());

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopUp = getLayoutInflater().inflate(R.layout.popup_jugar, null);

        dialogBuilder.setView(contactPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset del cronometro.
                PauseOffSet = 0;
                Frase frase = new Frase();
                frases.add(frase);
                tinyDB.putListObject("frase", frases);
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