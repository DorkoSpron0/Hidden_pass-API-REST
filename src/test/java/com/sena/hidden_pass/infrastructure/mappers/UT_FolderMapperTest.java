package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.PasswordDataProvider;
import com.sena.hidden_pass.domain.models.*;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UT_FolderMapperTest {

    @Autowired
    private FolderMapper folderMapper;

    @BeforeEach
    void init(){
        folderMapper = new FolderMapper();
    }

    @Test
    void testFolderModelToDBO(){
        // Given
        UUID folderId = UUID.randomUUID();

        UserModel userModel = new UserModel(
                UUID.randomUUID(),
                new EmailValueObject("nicky@nicky.com"),
                new UsernameValueObject("username"),
                "master_password",
                "http://image.co",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new SecurityCodesModel(UUID.randomUUID())
        );

        FolderModel folderModel = new FolderModel(
                "descriptionFolder",
                "icon",
                folderId,
                "folderName" ,
                userModel,
                new ArrayList<>(List.of(PasswordDataProvider.getPasswordModel()))
        );

        // When
        FolderDBO folderMapped = FolderMapper.folderModelToDBO(folderModel);

        // Then
        assertNotNull(folderMapped);

        assertEquals(folderModel.getId_folder(), folderMapped.getId_folder());
        assertEquals(folderModel.getName(), folderMapped.getName());
        assertEquals(folderModel.getDescription(), folderMapped.getDescription());
        assertEquals(folderModel.getIcon(), folderMapped.getIcon());
        assertEquals(folderModel.getUser().getId_usuario(), folderMapped.getUser().getId_usuario());
        assertEquals(folderModel.getUser().getUsername(), folderMapped.getUser().getUsername());
        assertEquals(folderModel.getUser().getEmail(), folderMapped.getUser().getEmail());
        assertEquals(folderModel.getPasswordModels().getFirst().getId_password(), folderMapped.getPasswords().getFirst().getId_password());
        assertEquals(folderModel.getPasswordModels().getFirst().getName(), folderMapped.getPasswords().getFirst().getName());
        assertEquals(folderModel.getPasswordModels().getFirst().getEmail_user(), folderMapped.getPasswords().getFirst().getEmail_user());
        assertEquals(folderModel.getPasswordModels().getFirst().getPassword(), folderMapped.getPasswords().getFirst().getPassword());
    }

    @Test
    void testFolderModelToDBOUserNull(){
        // Given
        UUID folderId = UUID.randomUUID();

        FolderModel folderModel = new FolderModel(
                "descriptionFolder",
                "icon",
                folderId,
                "folderName" ,
                null,
                new ArrayList<>(List.of(PasswordDataProvider.getPasswordModel()))
        );

        // When
        FolderDBO folderMapped = FolderMapper.folderModelToDBO(folderModel);

        // Then
        assertNotNull(folderMapped);

        assertEquals(folderModel.getId_folder(), folderMapped.getId_folder());
        assertEquals(folderModel.getName(), folderMapped.getName());
        assertEquals(folderModel.getDescription(), folderMapped.getDescription());
        assertEquals(folderModel.getIcon(), folderMapped.getIcon());
        assertNull(folderModel.getUser());
        assertEquals(folderModel.getPasswordModels().getFirst().getId_password(), folderMapped.getPasswords().getFirst().getId_password());
        assertEquals(folderModel.getPasswordModels().getFirst().getName(), folderMapped.getPasswords().getFirst().getName());
        assertEquals(folderModel.getPasswordModels().getFirst().getEmail_user(), folderMapped.getPasswords().getFirst().getEmail_user());
        assertEquals(folderModel.getPasswordModels().getFirst().getPassword(), folderMapped.getPasswords().getFirst().getPassword());
    }

    @Test
    void testFolderModelToDBOWithoutPasswords(){
        // Given
        UUID folderId = UUID.randomUUID();

        UserModel userModel = new UserModel(
                UUID.randomUUID(),
                new EmailValueObject("nicky@nicky.com"),
                new UsernameValueObject("username"),
                "master_password",
                "http://image.co",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new SecurityCodesModel(UUID.randomUUID())
        );

        FolderModel folderModel = new FolderModel(
                "descriptionFolder",
                "icon",
                folderId,
                "folderName" ,
                userModel,
                null
        );

        // When
        FolderDBO folderMapped = FolderMapper.folderModelToDBO(folderModel);

        // Then
        assertNotNull(folderMapped);

        assertEquals(folderModel.getId_folder(), folderMapped.getId_folder());
        assertEquals(folderModel.getName(), folderMapped.getName());
        assertEquals(folderModel.getDescription(), folderMapped.getDescription());
        assertEquals(folderModel.getIcon(), folderMapped.getIcon());
        assertEquals(folderModel.getUser().getId_usuario(), folderMapped.getUser().getId_usuario());
        assertEquals(folderModel.getUser().getUsername(), folderMapped.getUser().getUsername());
        assertEquals(folderModel.getUser().getEmail(), folderMapped.getUser().getEmail());
        assertInstanceOf(ArrayList.class, folderMapped.getPasswords());
        assertEquals(0, folderMapped.getPasswords().size());
    }

    @Test
    void testFolderDBOToModel(){
        // Given
        UUID folderId = UUID.randomUUID();
        UserDBO dbo = new UserDBO(
                UUID.randomUUID(),
                new UsernameValueObject("username"),
                new EmailValueObject("email@email.com"),
                "master_password",
                "url",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                null
        );
        FolderDBO folderModel = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", dbo, new ArrayList<>(List.of(PasswordDataProvider.getPasswordDBO())));

        // When
        FolderModel folderMapped = FolderMapper.folderDBOToModel(folderModel);

        // Then
        assertEquals(folderModel.getId_folder(), folderMapped.getId_folder());
        assertEquals(folderModel.getName(), folderMapped.getName());
        assertEquals(folderModel.getDescription(), folderMapped.getDescription());
        assertEquals(folderModel.getIcon(), folderMapped.getIcon());
        assertEquals(folderModel.getUser().getId_usuario(), folderMapped.getUser().getId_usuario());
        assertEquals(folderModel.getUser().getUsername(), folderMapped.getUser().getUsername());
        assertEquals(folderModel.getUser().getEmail(), folderMapped.getUser().getEmail());
        assertEquals(folderModel.getPasswords().getFirst().getId_password(), folderMapped.getPasswordModels().getFirst().getId_password());
        assertEquals(folderModel.getPasswords().getFirst().getName(), folderMapped.getPasswordModels().getFirst().getName());
        assertEquals(folderModel.getPasswords().getFirst().getEmail_user(), folderMapped.getPasswordModels().getFirst().getEmail_user());
        assertEquals(folderModel.getPasswords().getFirst().getPassword(), folderMapped.getPasswordModels().getFirst().getPassword());
    }

    @Test
    void testFolderDBOToModelUserNull(){
        // Given
        UUID folderId = UUID.randomUUID();

        FolderDBO folderModel = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", null, new ArrayList<>(List.of(PasswordDataProvider.getPasswordDBO())));

        // When
        FolderModel folderMapped = FolderMapper.folderDBOToModel(folderModel);

        // Then
        assertNotNull(folderMapped);

        assertEquals(folderModel.getId_folder(), folderMapped.getId_folder());
        assertEquals(folderModel.getName(), folderMapped.getName());
        assertEquals(folderModel.getDescription(), folderMapped.getDescription());
        assertEquals(folderModel.getIcon(), folderMapped.getIcon());
        assertNull(folderModel.getUser());
        assertEquals(folderModel.getPasswords().getFirst().getId_password(), folderMapped.getPasswordModels().getFirst().getId_password());
        assertEquals(folderModel.getPasswords().getFirst().getName(), folderMapped.getPasswordModels().getFirst().getName());
        assertEquals(folderModel.getPasswords().getFirst().getEmail_user(), folderMapped.getPasswordModels().getFirst().getEmail_user());
        assertEquals(folderModel.getPasswords().getFirst().getPassword(), folderMapped.getPasswordModels().getFirst().getPassword());
    }

    @Test
    void testFolderDBOToWithoutPasswords(){
        // Given
        UUID folderId = UUID.randomUUID();
        UserDBO dbo = new UserDBO(
                UUID.randomUUID(),
                new UsernameValueObject("username"),
                new EmailValueObject("email@email.com"),
                "master_password",
                "url",
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                null
        );
        FolderDBO folderModel = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", dbo, null);

        // When
        FolderModel folderMapped = FolderMapper.folderDBOToModel(folderModel);

        // Then
        assertNotNull(folderMapped);

        assertEquals(folderModel.getId_folder(), folderMapped.getId_folder());
        assertEquals(folderModel.getName(), folderMapped.getName());
        assertEquals(folderModel.getDescription(), folderMapped.getDescription());
        assertEquals(folderModel.getIcon(), folderMapped.getIcon());
        assertEquals(folderModel.getUser().getId_usuario(), folderMapped.getUser().getId_usuario());
        assertEquals(folderModel.getUser().getUsername(), folderMapped.getUser().getUsername());
        assertEquals(folderModel.getUser().getEmail(), folderMapped.getUser().getEmail());
        assertInstanceOf(ArrayList.class, folderMapped.getPasswordModels());
        assertEquals(0, folderMapped.getPasswordModels().size());
    }

    @Test
    void testPassTest() {
        assertTrue(this.folderMapper.passTest());
    }
}
