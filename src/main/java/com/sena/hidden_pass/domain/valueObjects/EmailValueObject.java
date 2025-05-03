package com.sena.hidden_pass.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class EmailValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @NotBlank
    @NotNull
    private String email;

    public EmailValueObject() {
    }

    public EmailValueObject(String email) {
        if(!email.matches(EMAIL_PATTERN.toString())) throw new IllegalArgumentException("El correo electrónico no es válido. Debe seguir el formato: ejemplo@dominio.com");
        this.email = email;
    }

    @JsonValue
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailValueObject that = (EmailValueObject) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    @Override
    public String toString() {
        return "EmailValueObject{" +
                "email='" + email + '\'' +
                '}';
    }
}
