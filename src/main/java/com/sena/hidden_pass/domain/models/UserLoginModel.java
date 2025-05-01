package com.sena.hidden_pass.domain.models;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;

import java.util.UUID;

public class UserLoginModel {

    private UUID userId;
    private UsernameValueObject username;
    private EmailValueObject emailValueObject;
    private String token;
    private String urlImage;

    public UserLoginModel() {
    }

    public UserLoginModel(UUID userId, UsernameValueObject username, EmailValueObject emailValueObject, String token, String urlImage) {
        this.userId = userId;
        this.username = username;
        this.emailValueObject = emailValueObject;
        this.token = token;
        this.urlImage = urlImage;
    }

    public EmailValueObject getEmailValueObject() {
        return emailValueObject;
    }

    public void setEmailValueObject(EmailValueObject emailValueObject) {
        this.emailValueObject = emailValueObject;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UsernameValueObject getUsername() {
        return username;
    }

    public void setUsername(UsernameValueObject username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserLoginModel{" +
                "userId=" + userId +
                ", username=" + username +
                ", emailValueObject=" + emailValueObject +
                ", token='" + token + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
