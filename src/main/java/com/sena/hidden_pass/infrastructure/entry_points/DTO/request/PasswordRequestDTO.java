package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.Pattern;

public record PasswordRequestDTO(@Pattern(regexp = "^[A-Za-zñ\\s\\d]+$") String name,
                                String url,
                                String email_user,
                                String password,
                                String description,
                                String folder_name) {
}
