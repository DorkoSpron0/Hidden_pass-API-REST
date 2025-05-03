package com.sena.hidden_pass.domain.valueObjects;


import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class UsernameValueObject {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\d]{4,}$");

    @NotBlank
    @NotNull
    private String username;

    protected UsernameValueObject(){}

    public UsernameValueObject(String username) {
        if(!username.matches(USERNAME_PATTERN.toString())) throw new IllegalArgumentException("Debe tener al menos 4 caracteres y solo puede contener letras y números.");
        this.username = username;
    }

    @JsonValue
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsernameValueObject that = (UsernameValueObject) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String toString() {
        return "UsernameValueObject{" +
                "username='" + username + '\'' +
                '}';
    }
}
