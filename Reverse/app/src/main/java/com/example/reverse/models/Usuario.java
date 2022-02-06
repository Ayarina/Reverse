package com.example.reverse.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String username;
    private String correo;

    //Constructor
    public Usuario(String username, String correo) {
        this.username = (!username.trim().isEmpty()) ? username : " ";
        this.correo = (!correo.trim().isEmpty()) ? correo : " ";
    }

    //Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
