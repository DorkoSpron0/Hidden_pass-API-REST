package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.PasswordDataProvider;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UT_PasswordMapperTest {

    private PasswordMapper passwordMapper;

    @BeforeEach
    void init(){
        passwordMapper = new PasswordMapper();
    }

    @Test
    void testPasswordModelToDBO(){
        // Given
        PasswordModel model = PasswordDataProvider.getPasswordModel();

        // Whem
        PasswordDBO result = PasswordMapper.passwordModelToDBO(model);

        // Then
        assertNotNull(result);

        assertEquals(model.getId_password(), result.getId_password());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getDescription(), result.getDescription());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getUrl(), result.getUrl());
        assertEquals(model.getDateTime(), result.getDateTime());
        assertEquals(model.getEmail_user(), result.getEmail_user());

        assertEquals(model.getId_folder().getId_folder(), result.getId_folder().getId_folder());
        assertEquals(model.getId_folder().getName(), result.getId_folder().getName());
    }

    @Test
    void testPasswordModelToDBOWithFolderNull(){
        // Given
        PasswordModel model = PasswordDataProvider.getPasswordModel();
        model.setId_folder(null);

        // Whem
        PasswordDBO result = PasswordMapper.passwordModelToDBO(model);

        // Then
        assertNotNull(result);

        assertEquals(model.getId_password(), result.getId_password());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getDescription(), result.getDescription());
        assertEquals(model.getPassword(), result.getPassword());
        assertEquals(model.getUrl(), result.getUrl());
        assertEquals(model.getDateTime(), result.getDateTime());
        assertEquals(model.getEmail_user(), result.getEmail_user());

        assertNull(result.getId_folder());
    }

    @Test
    void testPasswordDBOToModel(){
        // Given
        PasswordDBO dbo = PasswordDataProvider.getPasswordDBO();

        // When
        PasswordModel result = PasswordMapper.passwordDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_password(), result.getId_password());
        assertEquals(dbo.getName(), result.getName());
        assertEquals(dbo.getDescription(), result.getDescription());
        assertEquals(dbo.getPassword(), result.getPassword());
        assertEquals(dbo.getUrl(), result.getUrl());
        assertEquals(dbo.getDateTime(), result.getDateTime());
        assertEquals(dbo.getEmail_user(), result.getEmail_user());

        assertEquals(dbo.getId_folder().getId_folder(), result.getId_folder().getId_folder());
        assertEquals(dbo.getId_folder().getName(), result.getId_folder().getName());
    }

    @Test
    void testPasswordDBOToModelWithFolderNull(){
        // Given
        PasswordDBO dbo = PasswordDataProvider.getPasswordDBO();
        dbo.setId_folder(null);

        // When
        PasswordModel result = PasswordMapper.passwordDBOToModel(dbo);

        // Then
        assertNotNull(result);

        assertEquals(dbo.getId_password(), result.getId_password());
        assertEquals(dbo.getName(), result.getName());
        assertEquals(dbo.getDescription(), result.getDescription());
        assertEquals(dbo.getPassword(), result.getPassword());
        assertEquals(dbo.getUrl(), result.getUrl());
        assertEquals(dbo.getDateTime(), result.getDateTime());
        assertEquals(dbo.getEmail_user(), result.getEmail_user());

        assertNull(result.getId_folder());
    }

    @Test
    void testPassTest(){
        // When
        boolean result = this.passwordMapper.passTest();

        // Then
        assertTrue(result);
    }
}
