package com.sena.hidden_pass.domain.models;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordModel {
    private UUID id_password;
    private String name;
    private String url;
    private LocalDateTime dateTime;
    private String email_user;
    private String password;
    private String description;
    private FolderModel id_folder;

    public PasswordModel() {
    }

    public PasswordModel(UUID id_password, String name, String description, String email_user, String password, String url, LocalDateTime dateTime) {
        this.id_password = id_password;
        this.name = name;
        this.description = description;
        this.email_user = email_user;
        this.password = password;
        this.url = url;
        this.dateTime = dateTime;
    }

    public PasswordModel(UUID id_password, String name, String description, String email_user, String password, String url, LocalDateTime dateTime, FolderModel id_folder) {
        this.id_password = id_password;
        this.name = name;
        this.description = description;
        this.email_user = email_user;
        this.password = password;
        this.url = url;
        this.dateTime = dateTime;
        this.id_folder = id_folder;
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

    public FolderModel getId_folder() {
        return id_folder;
    }

    public void setId_folder(FolderModel id_folder) {
        this.id_folder = id_folder;
    }

    public UUID getId_password() {
        return id_password;
    }

    public void setId_password(UUID id_password) {
        this.id_password = id_password;
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

    @Override
    public String toString() {
        return "PasswordModel{" +
                "dateTime=" + dateTime +
                ", id_password=" + id_password +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", email_user='" + email_user + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", id_folder=" + id_folder +
                '}';
    }
}
