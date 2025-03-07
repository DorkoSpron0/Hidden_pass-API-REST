package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityNames;

import java.util.Set;
import java.util.UUID;

public interface NoteUseCases {
    Set<NoteDBO> getAllNotesByUser(UUID user_id);
    NoteDBO getNoteById(UUID id);
    NoteDBO createNote(NoteDBO note, UUID user_id, PriorityNames priority_name);
    NoteDBO updateNote(NoteDBO note, UUID note_id);
    String deleteNote(UUID note_id);
}
