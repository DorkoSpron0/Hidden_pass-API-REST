package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FolderRequestDTO(@NotNull @NotBlank String name,
                              @NotNull @NotBlank String icon,
                              @NotNull @NotBlank String description,
                              List<String> passwords) {
}
