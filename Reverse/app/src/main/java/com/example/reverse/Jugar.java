package com.example.reverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class Jugar extends AppCompatActivity{

    private TextView fraseText;
    private EditText fraseUsuario;
    private Button botonEmpezar;
    private Button botonTerminar;
    private Button botonVolver;
    private Chronometer cronometro;
    private long time;

    private ArrayList<Object> frases;
    private Frase frase;
    private TinyDB tinyDB;

    private FraseAdapter fraseAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        tinyDB = new TinyDB(this);
        //Inicializamos el ArrayList (comprobando antes si esta vacío o no)
        if(tinyDB.getListObject("frases", Frase.class) != null)
            frases = tinyDB.getListObject("frases", Frase.class);
        else
            frases = new ArrayList<>();

        //Inicializamos el adaptador
        fraseAdapter = new FraseAdapter(frases);

        fraseText = findViewById(R.id.frase);
        fraseUsuario = findViewById(R.id.usuario_frase);
        botonEmpezar = findViewById(R.id.boton_jugar);
        botonTerminar = findViewById(R.id.boton_terminar);
        botonVolver = findViewById(R.id.boton_volver);
        cronometro = findViewById(R.id.cronometro);

        //Sacamos los datos del intent
        Intent intent = getIntent();
        frase = (Frase) intent.getSerializableExtra("fraseJugar");
        fraseText.setText(frase.getFrase());

        botonTerminar.setVisibility(View.INVISIBLE);

        botonEmpezar.setEnabled(true);
        botonTerminar.setEnabled(false);
        botonVolver.setEnabled(true);

        botonEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonEmpezar.setEnabled(false);
                botonTerminar.setEnabled(true);
                cronometro.setBase(SystemClock.elapsedRealtime());

                cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {

                        if (SystemClock.elapsedRealtime() - cronometro.getBase() >= 600000){
                            cronometro.setBase(SystemClock.elapsedRealtime());
                            //Toast aqui para informar al usuario
                            Toast.makeText(Jugar.this, "El tiempo ha superado el permitido", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Jugar.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

                cronometro.start();
                botonTerminar.setVisibility(View.VISIBLE);
                botonTerminar.setEnabled(true);

                botonTerminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cronometro.stop();
                        time = SystemClock.elapsedRealtime() - cronometro.getBase();
                        botonEmpezar.setEnabled(false);
                        botonVolver.setEnabled(false);

                        contactPopUp();
                    }
                });
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Jugar.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void contactPopUp(){

        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder;

        dialogBuilder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.popup_jugar, null);

        TextView puntuacion = popup.findViewById(R.id.puntuacion_popup);
        TextView tiempo = popup.findViewById(R.id.tiempo_popup);
        Button reintentar = popup.findViewById(R.id.reintentar_popup);
        Button salir = popup.findViewById(R.id.salir_popup);
        puntuacion.setText(String.valueOf(puntuacionUsuario()));
        tiempo.setText(conversorTiempo());

        dialogBuilder.setView(popup);

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Asignación de la puntuacion y tiempo a la frase (Se prioriza una puntuacion alta al tiempo)
                if (Integer.parseInt(puntuacion.getText().toString()) > frase.getPuntuacion()){
                    frase.setPuntuacion(Integer.parseInt(puntuacion.getText().toString()));
                    frase.setTiempo(time);

                } else if ((Integer.parseInt(puntuacion.getText().toString()) == frase.getPuntuacion()) && (time < frase.getTiempo())){

                    frase.setTiempo(time);

                }
                else{
                    frase.setPuntuacion(Integer.parseInt(puntuacion.getText().toString()));
                    frase.setTiempo(time);

                }

                frases.add(frases.indexOf(frase), frase);
                fraseAdapter.notifyUpdate(frases.indexOf(frase));
                tinyDB.putListObject("frases", frases);

                Intent intent = new Intent(Jugar.this, MainActivity.class);
                startActivity(intent);

                alertDialog.dismiss();

            }
        });

        reintentar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                botonEmpezar.setEnabled(true);
                botonTerminar.setEnabled(false);
                fraseUsuario.setText("");
                //Reset del cronometro.
                cronometro.setBase(SystemClock.elapsedRealtime());
                time = 0;

                alertDialog.dismiss();
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