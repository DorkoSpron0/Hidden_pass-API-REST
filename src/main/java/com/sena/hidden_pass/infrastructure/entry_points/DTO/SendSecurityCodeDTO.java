package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class SendSecurityCodeDTO {

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El correo electrónico no es válido. Debe seguir el formato: ejemplo@dominio.com"
    )
    private String email;
}
