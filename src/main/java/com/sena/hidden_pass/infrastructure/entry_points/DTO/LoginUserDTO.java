package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class LoginUserDTO {

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El correo electrónico no es válido. Debe seguir el formato: ejemplo@dominio.com"
    )
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, un número y un símbolo especial."
    )
    private String master_password;

    public UserDBO toDomain(){
        return new UserDBO(
                this.email,
                new HashSet<>(),
                this.master_password,
                new HashSet<>(),
                new HashSet<>(),
                null,
                new HashSet<>(),
                null
        );
    }

    @Override
    public String toString() {
        return "LoginUserDTO{" +
                "email='" + email + '\'' +
                ", master_password='" + master_password + '\'' +
                '}';
    }
}
