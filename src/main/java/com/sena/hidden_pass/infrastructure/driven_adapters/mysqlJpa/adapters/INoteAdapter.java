package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.usecases.NoteUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.INoteRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class INoteAdapter implements NoteUseCases {

    private INoteRepository noteRepository;
    private IUserAdapter userAdapter;

    @Autowired
    private IPriorityRepository priorityRepository;

    @Override
    public Set<NoteDBO> getAllNotesByUser(UUID user_id) {
        UserDBO userFounded = userAdapter.getUserById(user_id);
        return userFounded.getNoteList();
    }

    @Override
    public NoteDBO getNoteById(UUID id) {
        return noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Note with id " + id + " not found"));
    }

    @Override
    public NoteDBO createNote(NoteDBO note, UUID user_id, PriorityNames priority_name) {

        PriorityDBO priority =  priorityRepository.getByName(priority_name).orElseThrow(() -> new IllegalArgumentException(""));

        note.setId_priority(priority);

        NoteDBO noteSaved = noteRepository.save(note);
        UserDBO userFounded = userAdapter.getUserById(user_id);
        userFounded.getNoteList().add(noteSaved);
        userAdapter.registerUser(userFounded);


        return noteSaved;
    }

    @Override
    public NoteDBO updateNote(NoteDBO note, UUID note_id) {

        NoteDBO noteDBO = getNoteById(note_id);

        noteDBO.setDescription(note.getDescription());
        noteDBO.setTitle(note.getTitle());
        noteDBO.setId_priority(note.getId_priority());

        return noteRepository.save(noteDBO);
    }

    @Override
    public String deleteNote(UUID note_id) {

        NoteDBO noteDBO = getNoteById(note_id);
        noteRepository.delete(noteDBO);

        return "Note deleted successfully";
    }
}
