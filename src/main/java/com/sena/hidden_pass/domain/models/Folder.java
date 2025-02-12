package com.sena.hidden_pass.domain.models;

import org.springframework.security.core.userdetails.User;

import java.util.UUID;

public class Folder {
    private UUID id_folder;
    private String name;
    private String icon;
    private String Description;
    private User id_user;

    public Folder() {
    }

    public Folder(String description, String icon, UUID id_folder, User id_user, String name) {
        Description = description;
        this.icon = icon;
        this.id_folder = id_folder;
        this.id_user = id_user;
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public UUID getId_folder() {
        return id_folder;
    }

    public void setId_folder(UUID id_folder) {
        this.id_folder = id_folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "Description='" + Description + '\'' +
                ", id_folder=" + id_folder +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
