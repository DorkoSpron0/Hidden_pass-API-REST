package com.sena.hidden_pass.infrastructure.entry_points.DTO.request;

import com.sena.hidden_pass.domain.models.PriorityNames;

public record NoteRequestDTO(PriorityNames priorityName,
                             String title,
                             String description) {
}
