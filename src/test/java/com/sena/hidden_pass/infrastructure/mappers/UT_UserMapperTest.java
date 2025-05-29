package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class UT_UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void init(){
        userMapper = new UserMapper();
    }

    @Test
    @DisplayName("Test should map user model to user dbo with all valid")
    void testUserModelToDBO() {
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        UserDBO result = UserMapper.userModelToDBO(model);

        // Then
        assertNotNull(result);

        assertEquals(model.getId_usuario(), result.getId_usuario());
        assertEquals(model.getEmail().getEmail(), result.getEmail());
        assertEquals(model.getMaster_password(), result.getMaster_password());
        assertEquals(model.getUsername().getUsername(), result.getUsername());
        assertEquals(model.getUrl_image(), result.getUrl_image());

        assertEquals(1, result.getPasswordList().size());
        assertEquals(1, result.getNoteList().size());
        assertEquals(1, result.getFolderList().size());
    }

    @Test
    @DisplayName("Test should map user model to user dbo with sets null")
    void testUserModelToDBONullSet(){
        // Given
        UserModel model = UserDataProvider.getUserModel();
        model.setPasswordList(null);
        model.setNoteList(null);
        model.setFolderList(null);
        model.setSecurityCodes(null);

        // When
        UserDBO result = UserMapper.userModelToDBO(model);

        assertEquals(model.getId_usuario(), result.getId_usuario());
        assertEquals(model.getEmail().getEmail(), result.getEmail());
        assertEquals(model.getMaster_password(), result.getMaster_password());
        assertEquals(model.getUsername().getUsername(), result.getUsername());
        assertEquals(model.getUrl_image(), result.getUrl_image());

        assertEquals(0, result.getPasswordList().size());
        assertEquals(0, result.getNoteList().size());
        assertEquals(0, result.getFolderList().size());
    }

    @Test
    @DisplayName("Test should map user model to user dbo with priority and folder null")
    void testUserModelToDBOPriorityAndFolderNull(){
        // Given
        UserModel model = UserDataProvider.getUserModel();
        model.getNoteList().stream().findFirst().ifPresent(noteModel -> noteModel.setId_priority(null));
        model.getPasswordList().stream().findFirst().ifPresent(noteModel -> noteModel.setId_folder(null));

        // When
        UserDBO result = UserMapper.userModelToDBO(model);

        assertEquals(model.getId_usuario(), result.getId_usuario());
        assertEquals(model.getEmail().getEmail(), result.getEmail());
        assertEquals(model.getMaster_password(), result.getMaster_password());
        assertEquals(model.getUsername().getUsername(), result.getUsername());
        assertEquals(model.getUrl_image(), result.getUrl_image());

        assertEquals(1, result.getPasswordList().size());
        assertEquals(1, result.getNoteList().size());
        assertEquals(1, result.getFolderList().size());

        assertTrue(result.getNoteList().stream()
                .findFirst()
                .map(note -> note.getId_priority() == null)
                .orElse(false));

        assertTrue(result.getPasswordList().stream()
                .findFirst()
                .map(note -> note.getId_folder() == null)
                .orElse(false));
    }

    @Test
    @DisplayName("Test should map user dbo to user model with all valid")
    void testUserDBOToModel(){
        // Given
        UserDBO dbo = UserDataProvider.getUserDBO();

        // When
        UserModel result = UserMapper.userDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_usuario(), result.getId_usuario());
        assertEquals(dbo.getEmail(), result.getEmail().getEmail());
        assertEquals(dbo.getMaster_password(), result.getMaster_password());
        assertEquals(dbo.getUsername(), result.getUsername().getUsername());
        assertEquals(dbo.getUrl_image(), result.getUrl_image());

        assertEquals(1, result.getPasswordList().size());
        assertEquals(1, result.getNoteList().size());
        assertEquals(1, result.getFolderList().size());
    }

    @Test
    @DisplayName("Test should map user dbo to model dbo with sets null")
    void testUserDBOToModelNullSet(){
        // Given
        UserDBO dbo = UserDataProvider.getUserDBO();
        dbo.setNoteList(null);
        dbo.setPasswordList(null);
        dbo.setFolderList(null);
        dbo.setSecurityCodes(null);

        // When
        UserModel result = UserMapper.userDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_usuario(), result.getId_usuario());
        assertEquals(dbo.getEmail(), result.getEmail().getEmail());
        assertEquals(dbo.getMaster_password(), result.getMaster_password());
        assertEquals(dbo.getUsername(), result.getUsername().getUsername());
        assertEquals(dbo.getUrl_image(), result.getUrl_image());

        assertEquals(0, result.getPasswordList().size());
        assertEquals(0, result.getNoteList().size());
        assertEquals(0, result.getFolderList().size());
    }

    @Test
    @DisplayName("Test should map user dbo to user model with priority and folder null")
    void testUserDBOToModelPriorityAndFolderNull(){
        // Given
        UserDBO dbo = UserDataProvider.getUserDBO();
        dbo.getNoteList().stream().findFirst().ifPresent(noteModel -> noteModel.setId_priority(null));
        dbo.getPasswordList().stream().findFirst().ifPresent(noteModel -> noteModel.setId_folder(null));

        // When
        UserModel result = UserMapper.userDBOToModel(dbo);

        assertEquals(dbo.getId_usuario(), result.getId_usuario());
        assertEquals(dbo.getEmail(), result.getEmail().getEmail());
        assertEquals(dbo.getMaster_password(), result.getMaster_password());
        assertEquals(dbo.getUsername(), result.getUsername().getUsername());
        assertEquals(dbo.getUrl_image(), result.getUrl_image());

        assertEquals(1, result.getPasswordList().size());
        assertEquals(1, result.getNoteList().size());
        assertEquals(1, result.getFolderList().size());

        assertTrue(result.getNoteList().stream()
                .findFirst()
                .map(note -> note.getId_priority() == null)
                .orElse(false));

        assertTrue(result.getPasswordList().stream()
                .findFirst()
                .map(note -> note.getId_folder() == null)
                .orElse(false));
    }

    @Test
    void testPassTests(){
        // WHen
        boolean result = this.userMapper.passTests();
        assertTrue(result);
    }
}
