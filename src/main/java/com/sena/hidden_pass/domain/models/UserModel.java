package com.sena.hidden_pass.domain.models;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;

import java.util.Set;
import java.util.UUID;

public class UserModel {

    private UUID id_usuario;
    private UsernameValueObject username;
    private EmailValueObject email;
    private String master_password;
    private String url_image;
    private Set<NoteModel> noteList;
    private Set<PasswordModel> passwordList;
    private Set<FolderModel> folderList;
    private SecurityCodesModel securityCodes;

    public UserModel() {
    }

    public UserModel(UUID id_usuario, EmailValueObject email, UsernameValueObject username, String master_password, String url_image, Set<PasswordModel> passwordList, Set<NoteModel> noteList, Set<FolderModel> folderList, SecurityCodesModel securityCodes) {
        this.id_usuario = id_usuario;
        this.email = email;
        this.username = username;
        this.master_password = master_password;
        this.url_image = url_image;
        this.passwordList = passwordList;
        this.noteList = noteList;
        this.folderList = folderList;
        this.securityCodes = securityCodes;
    }

    public EmailValueObject getEmail() {
        return email;
    }

    public void setEmail(EmailValueObject email) {
        this.email = email;
    }

    public Set<FolderModel> getFolderList() {
        return folderList;
    }

    public void setFolderList(Set<FolderModel> folderList) {
        this.folderList = folderList;
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

    public Set<NoteModel> getNoteList() {
        return noteList;
    }

    public void setNoteList(Set<NoteModel> noteList) {
        this.noteList = noteList;
    }

    public Set<PasswordModel> getPasswordList() {
        return passwordList;
    }

    public void setPasswordList(Set<PasswordModel> passwordList) {
        this.passwordList = passwordList;
    }

    public SecurityCodesModel getSecurityCodes() {
        return securityCodes;
    }

    public void setSecurityCodes(SecurityCodesModel securityCodes) {
        this.securityCodes = securityCodes;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public UsernameValueObject getUsername() {
        return username;
    }

    public void setUsername(UsernameValueObject username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserDBO{" +
                "email=" + email +
                ", id_usuario=" + id_usuario +
                ", username=" + username +
                ", master_password='" + master_password + '\'' +
                ", url_image='" + url_image + '\'' +
                ", noteList=" + noteList +
                ", passwordList=" + passwordList +
                ", folderList=" + folderList +
                ", securityCodes=" + securityCodes +
                '}';
    }
}
