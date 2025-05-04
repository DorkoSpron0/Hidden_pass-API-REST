package com.sena.hidden_pass.infrastructure.entry_points.DTO.response;

import com.sena.hidden_pass.domain.models.PriorityNames;

import java.util.UUID;

public record NoteInfoResponseDTO(UUID id_note,
                                  PriorityNames id_priority,
                                  String title,
                                  String description) {
}
