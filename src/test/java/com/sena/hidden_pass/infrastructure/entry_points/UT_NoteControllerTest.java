package com.sena.hidden_pass.infrastructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.NoteDataProvider;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.domain.usecases.NoteUseCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.NoteDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoteController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class UT_NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteUseCases noteUseCases;

    @Autowired
    private ObjectMapper objectMapper; // Jackson

    @AfterEach
    void cleanMocks(){
        Mockito.reset(noteUseCases);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public NoteUseCases noteUseCases(){
            return Mockito.mock(NoteUseCases.class);
        }
    }

    @Test
    void testGetNotesByUserId() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.noteUseCases.getAllNotesByUser(eq(userId))).thenReturn(Set.of(NoteDataProvider.getNoteModel()));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/notes/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].description").value("Description"));
    }

    @Test
    void testGetNotesByUserIdNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.noteUseCases.getAllNotesByUser(eq(userId))).thenThrow(new IllegalArgumentException("User not found"));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/notes/{user_id}", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testCreateNote() throws Exception {
        // Given
        NoteDTO note = new NoteDTO(PriorityNames.ALTA, "Title", "Description");
        UUID userId = UUID.randomUUID();

        // When
        when(this.noteUseCases.createNote(any(NoteModel.class), eq(userId), eq(note.getPriorityName()))).thenReturn(NoteDataProvider.getNoteModel());

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/notes/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(note.getTitle()))
                .andExpect(jsonPath("$.description").value(note.getDescription()));

        verify(this.noteUseCases).createNote(any(NoteModel.class), eq(userId), eq(note.getPriorityName()));
    }

    @Test
    void testCreateNoteNotFound() throws Exception {
        // Given
        NoteDTO note = new NoteDTO(PriorityNames.ALTA, "Title", "Description");
        UUID userId = UUID.randomUUID();

        // When
        when(this.noteUseCases.createNote(any(NoteModel.class), eq(userId), eq(note.getPriorityName()))).thenThrow(new IllegalArgumentException("User not found"));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/notes/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testUpdateNote() throws Exception {
        // Given
        NoteDTO note = NoteDataProvider.getNoteDTO();
        UUID noteId = UUID.randomUUID();

        NoteModel noteExpected = new NoteModel();
        noteExpected.setTitle(note.getTitle());
        noteExpected.setDescription(note.getDescription());
        noteExpected.setId_priority(new PriorityModel(UUID.randomUUID(), note.getPriorityName()));

        // When
        when(this.noteUseCases.updateNote(any(NoteModel.class), eq(noteId))).thenReturn(noteExpected);

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/notes/{note_id}", noteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(note.getTitle()))
                .andExpect(jsonPath("$.description").value(note.getDescription()))
                .andExpect(jsonPath("$.id_priority.name").value(note.getPriorityName().name()));

        verify(this.noteUseCases).updateNote(any(NoteModel.class), eq(noteId));
    }

    @Test
    void testUpdateNoteNotFound() throws Exception {
        // Given
        NoteDTO note = NoteDataProvider.getNoteDTO();
        UUID noteId = UUID.randomUUID();

        NoteModel noteExpected = new NoteModel();
        noteExpected.setTitle(note.getTitle());
        noteExpected.setDescription(note.getDescription());
        noteExpected.setId_priority(new PriorityModel(UUID.randomUUID(), note.getPriorityName()));

        // When
        when(this.noteUseCases.updateNote(any(NoteModel.class), eq(noteId))).thenThrow(new IllegalArgumentException("Note not found"));

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/notes/{note_id}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Note not found"));
    }

    @Test
    void testDeleteNote() throws Exception {
        // Given
        UUID noteId = UUID.randomUUID();

        // When
        when(this.noteUseCases.deleteNote(eq(noteId))).thenReturn("Note deleted successfully");

        // Then
        mockMvc.perform(delete("/api/v1/hidden_pass/notes/{note_id}", noteId))
                .andExpect(status().isOk())
                .andExpect(content().string("Note deleted successfully"));

        verify(this.noteUseCases).deleteNote(eq(noteId));
    }

    @Test
    void testDeleteNoteNotFound() throws Exception {
        // Given
        UUID noteId = UUID.randomUUID();

        // When
        when(this.noteUseCases.deleteNote(eq(noteId))).thenThrow(new IllegalArgumentException("Note not found"));

        // Then
        mockMvc.perform(delete("/api/v1/hidden_pass/notes/{note_id}", noteId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Note not found"));
    }

}
