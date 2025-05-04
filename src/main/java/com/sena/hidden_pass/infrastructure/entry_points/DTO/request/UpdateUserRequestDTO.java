package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

public record UpdateUserRequestDTO(String username,
                            String email,
                            String url_image) {
}
