package com.sena.hidden_pass.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class MasterPasswordValueObject {

    private static final Pattern MASTER_PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");

    @NotBlank
    @NotNull
    private String master_password;

    protected MasterPasswordValueObject() {
    }

    public MasterPasswordValueObject(String master_password) {
        if(!MASTER_PASSWORD_PATTERN.matcher(master_password).matches()) throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, un número y un símbolo especial.");
        this.master_password = master_password;
    }

    @JsonValue
    public String getMaster_password() {
        return master_password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterPasswordValueObject that = (MasterPasswordValueObject) o;
        return Objects.equals(master_password, that.master_password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(master_password);
    }

    @Override
    public String toString() {
        return "MasterPasswordValueObject{" +
                "master_password='" + master_password + '\'' +
                '}';
    }
}
