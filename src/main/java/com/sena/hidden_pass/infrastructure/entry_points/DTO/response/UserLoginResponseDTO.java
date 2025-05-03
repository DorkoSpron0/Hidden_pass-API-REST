package com.sena.hidden_pass.infrastructure.entry_points.DTO.response;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;

import java.util.UUID;

public record UserLoginResponseDTO(UUID userId,
                                   String username,
                                   String email,
                                   String token,
                                   String urlImage) {
}
