package com.sena.hidden_pass.infrastructure.entry_points.DTO.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PasswordInfoResponseDTO(UUID id_password,
                                      String name,
                                      String url,
                                      LocalDateTime dateTime,
                                      String email_user,
                                      String password,
                                      String description,
                                      UUID id_folder) {
}
