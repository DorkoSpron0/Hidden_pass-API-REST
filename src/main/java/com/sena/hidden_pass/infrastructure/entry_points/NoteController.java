package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters.INoteAdapter;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.NoteDTO;
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
    public Set<NoteDBO> getNotesByUserId(@PathVariable UUID user_id){
        return noteAdapter.getAllNotesByUser(user_id);
    }

    @PostMapping("/{user_id}")
    public NoteDBO createNote(@RequestBody NoteDTO noteDTO, @PathVariable UUID user_id){
        return noteAdapter.createNote(noteDTO.toDomain(priorityRepository), user_id, noteDTO.getPriorityName());
    }
}
