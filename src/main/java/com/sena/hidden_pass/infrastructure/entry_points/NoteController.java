package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.domain.usecases.NoteUseCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.NoteRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.NoteInfoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/hidden_pass/notes")
public class NoteController {

    private NoteUseCases noteAdapter;

    public NoteController(NoteUseCases noteAdapter) {
        this.noteAdapter = noteAdapter;
    }

    @GetMapping("/{user_id}")
    public Set<NoteInfoResponseDTO> getNotesByUserId(@PathVariable UUID user_id){

        Set<NoteModel> notes = noteAdapter.getAllNotesByUser(user_id);

        return notes.stream()
                .map(this::modelToDTO).collect(Collectors.toSet());
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<NoteInfoResponseDTO> createNote(@RequestBody NoteRequestDTO noteDTO, @PathVariable UUID user_id){
        NoteModel note = noteAdapter.createNote(new NoteModel(
                null,
                noteDTO.title(),
                noteDTO.description(),
                new PriorityModel(
                        null,
                        noteDTO.priorityName()
                )
        ), user_id, noteDTO.priorityName());

        return new ResponseEntity<>(this.modelToDTO(note), HttpStatus.CREATED);
    }

    @PutMapping("/{note_id}")
    public ResponseEntity<NoteInfoResponseDTO> updateNote(@RequestBody NoteRequestDTO noteDTO, @PathVariable UUID note_id){

        NoteModel model = noteAdapter.updateNote(new NoteModel(
                null,
                noteDTO.title(),
                noteDTO.description(),
                new PriorityModel(
                        null,
                        noteDTO.priorityName()
                )
        ), note_id);

        return ResponseEntity.status(HttpStatus.OK).body(this.modelToDTO(model));
    }

    @DeleteMapping("/{note_id}")
    public ResponseEntity<String> deleteNote(@PathVariable UUID note_id){
        String result = noteAdapter.deleteNote(note_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    private NoteInfoResponseDTO modelToDTO(NoteModel model){
        return new NoteInfoResponseDTO(
                model.getId_note(),
                model.getId_priority().getName(),
                model.getTitle(),
                model.getDescription()
        );
    }
}
