package com.example.reverse.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String username;
    private String apellidos;
    private String numero;
    private String correo;

    //Constructor
    public Usuario(String username, String apellidos, String numero, String correo) {
        this.username = (!username.trim().isEmpty()) ? username : " ";
        this.apellidos = (!apellidos.trim().isEmpty()) ? apellidos : " ";
        this.numero = (!numero.trim().isEmpty()) ? numero : " ";
        this.correo = (!correo.trim().isEmpty()) ? correo : " ";
    }

    //Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
