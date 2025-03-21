package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters.INoteAdapter;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.NoteDTO;
import com.sena.hidden_pass.infrastructure.mappers.NoteMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/hidden_pass/notes")
public class NoteController {

    private INoteAdapter noteAdapter;
    private IPriorityRepository priorityRepository;

    @GetMapping("/{user_id}")
    public Set<NoteModel> getNotesByUserId(@PathVariable UUID user_id){
        return noteAdapter.getAllNotesByUser(user_id);
    }

    @PostMapping("/{user_id}")
    public NoteModel createNote(@Valid @RequestBody NoteDTO noteDTO, @PathVariable UUID user_id){
        return noteAdapter.createNote(NoteMapper.noteDTOToModel(noteDTO), user_id, noteDTO.getPriorityName());
    }

    @PutMapping("/{note_id}")
    public NoteModel updateNote(@RequestBody NoteDTO noteDTO, @PathVariable UUID note_id){
        return noteAdapter.updateNote(NoteMapper.noteDTOToModel(noteDTO), note_id);
    }

    @DeleteMapping("/{note_id}")
    public String deleteNote(@PathVariable UUID note_id){
        return noteAdapter.deleteNote(note_id);
    }
}
