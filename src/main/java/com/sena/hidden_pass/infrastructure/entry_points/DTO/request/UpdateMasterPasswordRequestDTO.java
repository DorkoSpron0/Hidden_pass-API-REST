package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMasterPasswordRequestDTO(@NotNull @NotBlank String current_password,
                                             @NotNull @NotBlank String new_password) {
}
