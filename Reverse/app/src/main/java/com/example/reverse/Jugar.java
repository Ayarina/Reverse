package com.example.reverse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reverse.ui.home.HomeFragment;

import java.util.ArrayList;

public class Jugar extends AppCompatActivity {

    private TextView fraseText;
    private EditText fraseUsuario;
    private TextView cronometro;
    private Button boton;

    //PopuUp

    private ArrayList<Object> frases;
    private Frase frase;
    private TinyDB tinyDB;

    private Thread crono;
    private Handler handler = new Handler();
    private int milisegundos = 0, segundos = 0, minutos = 0;
    String mili="", seg="", min="";
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        tinyDB = new TinyDB(this);

        fraseText = findViewById(R.id.frase);
        fraseUsuario = findViewById(R.id.usuario_frase);
        boton = findViewById(R.id.boton_jugar);
        cronometro = findViewById(R.id.cronometro);

        //Sacamos los datos del intent
        Intent intent = getIntent();
        frase = (Frase) intent.getSerializableExtra("fraseJugar");
        fraseText.setText(frase.getFrase());

        crono = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (isPlaying){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        milisegundos++;
                        if (milisegundos == 599){
                            segundos++;
                            milisegundos = 0;
                        }

                        if (segundos == 59){
                            minutos++;
                            segundos = 0;
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (milisegundos < 10){
                                    mili = "00"+milisegundos;
                                }
                                else if (milisegundos < 100){
                                    mili = "0"+milisegundos;
                                }
                                else {
                                    mili = ""+milisegundos;
                                }

                                if (segundos < 10){
                                    seg = "0"+segundos;
                                }
                                else {
                                    seg = ""+segundos;
                                }

                                if (minutos < 10){
                                    min = "0"+minutos;
                                }
                                else {
                                    min = ""+minutos;
                                }
                                cronometro.setText(min+":"+seg+","+mili);
                            }
                        });
                    }
                }
            }
        });
        crono.start();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPlaying = true;


                boton.setText("Acabar");

                //Si el usuario se pasa de este tiempo, inmediatamente será enviado al HomeFragment.
                if (minutos == 59 && segundos == 59 && milisegundos == 999){
                    isPlaying = false;

                    //Toast aqui para informar al usuario

                    Intent intent = new Intent(Jugar.this, HomeFragment.class);
                    startActivity(intent);
                }

                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPlaying = false;
                        contactPopUp();
                    }
                });
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
        tiempo.setText(min+":"+seg+","+mili);

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopUp = getLayoutInflater().inflate(R.layout.popup_jugar, null);

        dialogBuilder.setView(contactPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Asignación de la puntuacion y tiempo a la frase (Se prioriza una puntuacion alta al tiempo)
                if (Integer.parseInt(puntuacion.getText().toString()) > frase.getPuntuacion() || frase.getPuntuacion() == 0){

                    frase.setPuntuacion(Integer.parseInt(puntuacion.getText().toString()));
                    frase.setTiempo(convertirAMilisegundos());
                    frases.add(frase);
                    tinyDB.putListObject("frases", frases);

                } else if (Integer.parseInt(puntuacion.getText().toString()) == frase.getPuntuacion()){

                    frase.setTiempo(convertirAMilisegundos());
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
                minutos = 0;
                segundos = 0;
                milisegundos = 0;

                //setText del cronometro reseteado
                cronometro.setText("00:00,000");

                boton.setText("Empezar");

                dialog.dismiss();
            }
        });
    }

    private long convertirAMilisegundos (){
        return ((minutos * 1000L)*60)+(segundos * 1000L)+milisegundos;
    }
/*
    private String conversorTiempo(){

        int minutos, segundos, milisegundos;

        milisegundos = (int) frase.getTiempo()%1000;
        segundos = (int) ((frase.getTiempo()-milisegundos)/1000)%60;
        minutos = (int) ((((frase.getTiempo()-milisegundos)/1000)-segundos)/60)%60;

        return minutos+":"+segundos+","+milisegundos;
    }

 */

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