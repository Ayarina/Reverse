package com.example.reverse;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class Jugar extends AppCompatActivity {

    private TextView fraseText;
    private EditText fraseUsuario;
    private Button empezar;
    private Chronometer cronometro;

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
        empezar = findViewById(R.id.empezar);
        cronometro = findViewById(R.id.cronometro);

        frases = tinyDB.getListObject("Frase", Frase.class);



        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometro.setBase(SystemClock.elapsedRealtime() - PauseOffSet);
                cronometro.start();
                isPlaying = true;

                fraseUsuario.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        cronometro.stop();
                        PauseOffSet = SystemClock.elapsedRealtime() - cronometro.getBase();
                        isPlaying = false;

                        /*
                         Reset del cronometro.
                            PauseOffSet = 0;
                         */

                        if (editable.equals(fraseText.getText())){
                            //frase = new Frase(frase.getPuntuacionMaxima(), PauseOffSet);
                            frases.add(frase);
                            tinyDB.putListObject("Frase", frases);

                        }
                    }
                });
            }
        });
    }






}