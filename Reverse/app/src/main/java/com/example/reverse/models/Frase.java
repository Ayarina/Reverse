package com.example.reverse.models;

import android.os.SystemClock;

import java.io.Serializable;

public class Frase implements Serializable {

    private String frase;
    private String fraseInvertida;
    private int puntuacionMaxima;
    private int puntuacion;
    private long tiempo;

    public Frase(String frase) {
        this.frase = frase;
        this.fraseInvertida = invertirFrase();
        this.puntuacionMaxima = puntuacionMaxima();
        this.puntuacion = 0;
        this.tiempo = 0L;
    }

    public String getFrase() {
        return frase;
    }

    public String getFraseInvertida() {
        return fraseInvertida;
    }

    public int getPuntuacionMaxima() {
        return puntuacionMaxima;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    private int puntuacionMaxima(){

        int longitudFrase = (int) frase.chars().filter(ch -> ch != ' ').count();

        if (longitudFrase <= 30){
            return 50;
        } else if (longitudFrase <= 50){
            return 100;
        } else if (longitudFrase <= 100){
            return 150;
        } else {
            return 200;
        }
    }

    private String invertirFrase(){
        StringBuilder fraserev = new StringBuilder(frase);

        return fraserev.reverse().toString();
    }
}
