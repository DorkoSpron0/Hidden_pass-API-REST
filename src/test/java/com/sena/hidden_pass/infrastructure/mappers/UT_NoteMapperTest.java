package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.NoteDataProvider;
import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UT_NoteMapperTest {

    private NoteMapper noteMapper;

    @BeforeEach
    void init(){
        noteMapper = new NoteMapper();
    }

    @Test
    @DisplayName("should map note model to dbo with all parameters")
    void testNoteModelToDBO(){
        // Given
        NoteModel model = NoteDataProvider.getNoteModel();

        // When
        NoteDBO result = NoteMapper.noteModelToDBO(model);

        // Then
        assertNotNull(result);

        assertEquals(model.getId_note(), result.getId_note());
        assertEquals(model.getTitle(), result.getTitle());
        assertEquals(model.getDescription(), result.getDescription());
        assertEquals(model.getId_priority().getId_priority(), result.getId_priority().getId_priority());
        assertEquals(model.getId_priority().getName(), result.getId_priority().getName());
    }

    @Test
    @DisplayName("should map note model to dbo with priority null")
    void testNoteModelToDBOWithPriorityNull(){
        // Given
        NoteModel model = NoteDataProvider.getNoteModel();
        model.setId_priority(null);

        // When
        NoteDBO result = NoteMapper.noteModelToDBO(model);

        // Then
        assertNotNull(result);

        assertEquals(model.getId_note(), result.getId_note());
        assertEquals(model.getTitle(), result.getTitle());
        assertEquals(model.getDescription(), result.getDescription());

        assertNull(result.getId_priority());
    }

    @Test
    @DisplayName("should map note dbo to model with all parameters")
    void testNoteDBOToModel(){
        // Given
        NoteDBO dbo = NoteDataProvider.getNoteDBO();

        // When
        NoteModel result = NoteMapper.noteDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_note(), result.getId_note());
        assertEquals(dbo.getTitle(), result.getTitle());
        assertEquals(dbo.getDescription(), result.getDescription());
        assertEquals(dbo.getId_priority().getId_priority(), result.getId_priority().getId_priority());
        assertEquals(dbo.getId_priority().getName(), result.getId_priority().getName());
    }

    @Test
    @DisplayName("should map note dbo to model with priority null")
    void testNoteDBOToModelWithPriorityNull(){
        // Given
        NoteDBO dbo = NoteDataProvider.getNoteDBO();
        dbo.setId_priority(null);

        // When
        NoteModel result = NoteMapper.noteDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_note(), result.getId_note());
        assertEquals(dbo.getTitle(), result.getTitle());
        assertEquals(dbo.getDescription(), result.getDescription());

        assertNull(result.getId_priority());
    }

    @Test
    void testPassTest(){
        // When
        boolean result = this.noteMapper.passTest();
        // Then
        assertTrue(result);
    }
}
