package com.sena.hidden_pass.infrastructure.entry_points.DTO.response;

import java.util.List;
import java.util.UUID;

public record FolderInfoResponseDTO(UUID id_folder,
                                    String name,
                                    String icon,
                                    String Description,
                                    List<PasswordInfoResponseDTO> passwordModels) {
}
