package com.sena.hidden_pass.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Password {

    private UUID id_password;
    private UserModel id_user;
    private String name;
    private String url;
    private LocalDateTime dateTime;
    private String email_user;
    private String password;
    private String description;
    private Folder id_folder;

    public Password() {
    }

    public Password(LocalDateTime dateTime, String description, String email_user, Folder id_folder, UUID id_password, UserModel id_user, String name, String password, String url) {
        this.dateTime = dateTime;
        this.description = description;
        this.email_user = email_user;
        this.id_folder = id_folder;
        this.id_password = id_password;
        this.id_user = id_user;
        this.name = name;
        this.password = password;
        this.url = url;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public UUID getId_password() {
        return id_password;
    }

    public void setId_password(UUID id_password) {
        this.id_password = id_password;
    }

    public UserModel getId_user() {
        return id_user;
    }

    public void setId_user(UserModel id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Folder getId_folder() {
        return id_folder;
    }

    public void setId_folder(Folder id_folder) {
        this.id_folder = id_folder;
    }

    @Override
    public String toString() {
        return "Password{" +
                "dateTime=" + dateTime +
                ", id_password=" + id_password +
                ", id_user=" + id_user +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", email_user='" + email_user + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
