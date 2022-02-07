package com.example.reverse.models;

import android.os.SystemClock;

import java.io.Serializable;

public class Frase implements Serializable {

    private String frase;
    private String fraseInvertida;
    private int puntuacionMaxima;

    public Frase(String frase) {
        this.frase = frase;
        this.fraseInvertida = invertirFrase();
        this.puntuacionMaxima = puntuacionMaxima();
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

    private int puntuacionMaxima(){

        int longitudFrase = (int) frase.chars().filter(ch -> ch != ' ').count();
        int puntuacionMax;

        if (longitudFrase <= 30){
            puntuacionMax = 50;
        } else if (longitudFrase <= 50){
            puntuacionMax = 100;
        } else if (longitudFrase <= 100){
            puntuacionMax = 150;
        } else {
            puntuacionMax = 200;
        }

        return puntuacionMax;
    }

    private String invertirFrase(){
        StringBuilder fraserev = new StringBuilder(frase);

        return fraserev.reverse().toString();
    }
}
