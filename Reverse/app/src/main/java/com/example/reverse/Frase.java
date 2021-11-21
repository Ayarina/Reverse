package com.example.reverse;

import java.io.Serializable;

public class Frase implements Serializable {

    private String frase;
    private String fraseInvertida;
    private int puntuacionMaxima;
    private int puntuacion;
    private long tiempo;

    public Frase(String frase, String fraseInvertida, int puntuacionMaxima, int puntuacion, long tiempo) {
        this.frase = frase;
        this.fraseInvertida = fraseInvertida;
        this.puntuacionMaxima = puntuacionMaxima;
        this.puntuacion = puntuacion;
        this.tiempo = tiempo;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getFraseInvertida() {
        return fraseInvertida;
    }

    public void setFraseInvertida(String fraseInvertida) {
        this.fraseInvertida = fraseInvertida;
    }

    public int getPuntuacionMaxima() {
        return puntuacionMaxima;
    }

    public void setPuntuacionMaxima(int puntuacionMaxima) {
        this.puntuacionMaxima = puntuacionMaxima;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion, String frase) {
        this.puntuacion = puntuacion;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public static int puntuacionMaxima(String frase){

        int longitudFrase = (int) frase.chars().filter(ch -> ch != ' ').count();
        int puntosTotales;

        if (longitudFrase <= 30){
            puntosTotales = 10;
        } else if (longitudFrase <= 50){
            puntosTotales = 15;
        } else if (longitudFrase <= 100){
            puntosTotales = 20;
        } else {
            puntosTotales = 30;
        }

        return puntosTotales;
    }

    public static String invertirFrase(String frase){

        StringBuilder fraserev = new StringBuilder(frase);
        frase = fraserev.reverse().toString();

        return frase;
    }
}
