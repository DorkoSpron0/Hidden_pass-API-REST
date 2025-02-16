package com.sena.hidden_pass.domain.models;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import jakarta.persistence.OneToMany;

import java.util.Set;
import java.util.UUID;

public class UserModel {

    private UUID id_usuario;
    private String username;
    private String email;
    private String master_password;
    private Set<Note> noteList;
    private Set<Password> passwordList;
    private Set<Folder> folderList;



    public UserModel() {
    }

    public UserModel(String email, Set<Folder> folderList, UUID id_usuario, String master_password, Set<Note> noteList, Set<Password> passwordList, String username) {
        this.email = email;
        this.folderList = folderList;
        this.id_usuario = id_usuario;
        this.master_password = master_password;
        this.noteList = noteList;
        this.passwordList = passwordList;
        this.username = username;
    }

    public UserModel(String email, String username, Set<Password> passwordList, Set<Note> noteList, String master_password, Set<Folder> folderList) {
        this.email = email;
        this.username = username;
        this.passwordList = passwordList;
        this.noteList = noteList;
        this.master_password = master_password;
        this.folderList = folderList;
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

    public Set<Folder> getFolderList() {
        return folderList;
    }

    public void setFolderList(Set<Folder> folderList) {
        this.folderList = folderList;
    }

    public Set<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(Set<Note> noteList) {
        this.noteList = noteList;
    }

    public Set<Password> getPasswordList() {
        return passwordList;
    }

    public void setPasswordList(Set<Password> passwordList) {
        this.passwordList = passwordList;
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
