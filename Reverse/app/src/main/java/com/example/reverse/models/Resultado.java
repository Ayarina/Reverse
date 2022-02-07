package com.example.reverse.models;

import java.io.Serializable;

public class Resultado implements Serializable {

    private int puntuacion;
    private long tiempo;

    public Resultado (){
        puntuacion = 0;
        tiempo = 0L;
    }

    public Resultado (int puntuacion, long tiempo){
        this.puntuacion = puntuacion;
        this.tiempo = tiempo;
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
}
