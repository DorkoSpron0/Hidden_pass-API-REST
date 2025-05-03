package com.sena.hidden_pass.infrastructure.entry_points.DTO.response;

import java.util.UUID;

public record RegisterUserResponseDTO(UUID id_usuario,
                                      String username,
                                      String email,
                                      String master_password,
                                      String url_image) {
}
