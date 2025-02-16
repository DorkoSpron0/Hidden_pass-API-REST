package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.Note;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;

import java.util.List;

public interface NoteUseCases {
    List<NoteDBO> getAllNotes();
    NoteDBO getNoteById(Note note);
    NoteDBO createNote(Note note);
    NoteDBO updateNote(Note note);
    NoteDBO deleteNote(Note note);
}
