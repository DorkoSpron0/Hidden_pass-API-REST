package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

public record ResetMasterPasswordRequestDTO(String email,
                                            String new_password) {
}
