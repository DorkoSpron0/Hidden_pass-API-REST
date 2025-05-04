package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.Pattern;

public record SendSecurityCodeRequestDTO(
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El correo electrónico no es válido. Debe seguir el formato: ejemplo@dominio.com") String email) {
}
