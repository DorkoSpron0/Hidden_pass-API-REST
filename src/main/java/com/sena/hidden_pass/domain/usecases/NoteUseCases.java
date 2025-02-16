package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.Note;

import java.util.List;

public interface NoteUseCases {
    List<Note> getAllNotes();
    Note getNoteById(Note note);
    Note createNote(Note note);
    Note updateNote(Note note);
    Note deleteNote(Note note);
}
