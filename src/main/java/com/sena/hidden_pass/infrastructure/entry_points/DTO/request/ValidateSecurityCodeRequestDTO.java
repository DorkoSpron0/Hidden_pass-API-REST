package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

public record ValidateSecurityCodeRequestDTO(String email,
                                            String securityCode) {
}
