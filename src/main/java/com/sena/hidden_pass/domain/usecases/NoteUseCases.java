package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;

import java.util.List;

public interface NoteUseCases {
    List<NoteDBO> getAllNotes();
    NoteDBO getNoteById(NoteDBO note);
    NoteDBO createNote(NoteDBO note);
    NoteDBO updateNote(NoteDBO note);
    NoteDBO deleteNote(NoteDBO note);
}
