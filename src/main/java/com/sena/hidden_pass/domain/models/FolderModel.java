package com.sena.hidden_pass.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FolderModel {

    private UUID id_folder;
    private String name;
    private String icon;
    private String Description;
    private UserModel user;

    private List<PasswordModel> passwordModels = new ArrayList<>();

    public FolderModel() {
    }

    public FolderModel(String description, String icon, UUID id_folder, String name, UserModel user,List<PasswordModel> passwordModels) {
        Description = description;
        this.icon = icon;
        this.id_folder = id_folder;
        this.name = name;
        this.passwordModels = passwordModels;
        this.user = user;
    }

    public FolderModel(UUID id_folder, String name, String description, String icon, UserModel user) {
        this.id_folder = id_folder;
        this.name = name;
        Description = description;
        this.icon = icon;
        this.user = user;
    }

    public FolderModel(UUID id_folder, String name, String description, String icon) {
        this.id_folder = id_folder;
        this.name = name;
        Description = description;
        this.icon = icon;
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

    public List<PasswordModel> getPasswordModels() {
        return passwordModels;
    }

    public void setPasswordModels(List<PasswordModel> passwordModels) {
        this.passwordModels = passwordModels;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "FolderModel{" +
                "Description='" + Description + '\'' +
                ", id_folder=" + id_folder +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
