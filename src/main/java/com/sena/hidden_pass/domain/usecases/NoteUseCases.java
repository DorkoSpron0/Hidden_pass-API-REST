package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityNames;

import java.util.Set;
import java.util.UUID;

public interface NoteUseCases {
    Set<NoteModel> getAllNotesByUser(UUID user_id);
    NoteModel getNoteById(UUID id);
    NoteModel createNote(NoteModel note, UUID user_id, PriorityNames priority_name);
    NoteModel updateNote(NoteModel note, UUID note_id);
    String deleteNote(UUID note_id);
}
