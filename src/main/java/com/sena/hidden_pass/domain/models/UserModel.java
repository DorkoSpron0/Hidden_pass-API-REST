package com.sena.hidden_pass.domain.models;

import java.util.UUID;

public class UserModel {

    private UUID id_usuario;
    private String username;
    private String email;
    private String master_password;

    public UserModel() {
    }

    public UserModel(String email, UUID id_usuario, String master_password, String username) {
        this.email = email;
        this.id_usuario = id_usuario;
        this.master_password = master_password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(UUID id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getMaster_password() {
        return master_password;
    }

    public void setMaster_password(String master_password) {
        this.master_password = master_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", id_usuario=" + id_usuario +
                ", username='" + username + '\'' +
                ", master_password='" + master_password + '\'' +
                '}';
    }
}
