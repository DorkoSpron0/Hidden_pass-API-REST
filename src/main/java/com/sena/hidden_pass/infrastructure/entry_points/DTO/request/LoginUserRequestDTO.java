package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginUserRequestDTO(@NotNull @NotBlank String email,
                                  @NotNull @NotBlank String master_password) {
}
