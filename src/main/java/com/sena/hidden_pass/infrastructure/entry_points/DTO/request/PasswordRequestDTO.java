package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record PasswordRequestDTO(@Pattern(regexp = "^[A-Za-z\\s\\d]{4,}$") String name,
                                @NotNull @NotBlank String url,
                                @NotNull LocalDateTime dateTime,
                                @NotNull @NotBlank String email_user,
                                @NotNull @NotBlank String password,
                                @NotNull @NotBlank String description,
                                String folder_name) {
}
