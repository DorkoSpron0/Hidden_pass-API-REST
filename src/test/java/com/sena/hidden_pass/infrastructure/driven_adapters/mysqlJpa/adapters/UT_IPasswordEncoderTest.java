package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.PasswordDataProvider;
import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.PasswordMapper;
import com.sena.hidden_pass.infrastructure.utils.AESUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UT_IPasswordEncoderTest {

    @Mock
    private UserUseCases userAdapter;

    @Mock
    private IPasswordRepository passwordRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private AESUtil aesUtil;

    @InjectMocks
    private IPasswordAdapter passwordAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void clean(){
        reset(userRepository, userAdapter, passwordRepository, aesUtil);
    }

    @Test
    void testGetAllPasswordSuccessfully() throws Exception {
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.aesUtil.decrypt(anyString())).thenReturn("passwordEncrypted");

        Set<PasswordModel> passwords = this.passwordAdapter.getAllPassword(id);

        // Then
        assertNotNull(passwords);

        assertTrue(passwords.stream().anyMatch(passwordModel -> passwordModel.getPassword().equals("passwordEncrypted")));
        assertEquals(1, passwords.size());
    }

    @Test
    void testGetAllPasswordUserNotFound() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userAdapter.getUserById(any(UUID.class))).thenThrow(new IllegalArgumentException("User with id " + id + " not found"));

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.passwordAdapter.getAllPassword(id);
        });

        assertEquals("User with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testGetAllPasswordAESException() throws Exception {
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.aesUtil.decrypt(anyString())).thenThrow(RuntimeException.class);


        // Then
        assertThrows(RuntimeException.class, () -> {
            this.passwordAdapter.getAllPassword(id);
        });
    }

    @Test
    void testGetPasswordByIdSuccessfully() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.of(PasswordDataProvider.getPasswordDBO()));
        when(this.aesUtil.decrypt(anyString())).thenReturn("passwordDecrypted");

        PasswordModel model = this.passwordAdapter.getPasswordById(id);

        // Then
        assertNotNull(model);

        assertNotNull(model.getName());
        assertNotNull(model.getPassword());
        assertNotNull(model.getDescription());

        assertEquals("passwordDecrypted", model.getPassword());
    }

    @Test
    void testGetPasswordByIdNotFound() {
        // given
        UUID id = UUID.randomUUID();

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.passwordAdapter.getPasswordById(id);
        });

        assertEquals("Password not found", exception.getMessage());
    }

    @Test
    void testGetPasswordByIdAESException() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.of(PasswordDataProvider.getPasswordDBO()));
        when(this.aesUtil.decrypt(anyString())).thenThrow(RuntimeException.class);

        // Then
        assertThrows(RuntimeException.class, () -> {
            this.passwordAdapter.getPasswordById(id);
        });
    }

    @Test
    void testCreatePasswordSuccessfully() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID user_id = UUID.randomUUID();

        ArgumentCaptor<PasswordDBO> passwordSavedCaptor = ArgumentCaptor.forClass(PasswordDBO.class);

        // When
        when(this.aesUtil.encrypt(anyString())).thenReturn("passwordEncrypted");
        when(this.passwordRepository.save(any(PasswordDBO.class))).thenReturn(PasswordDataProvider.getPasswordDBO());

        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());

        PasswordModel model = this.passwordAdapter.createPassword(password, user_id);

        // THen
        assertNotNull(model);

        verify(this.passwordRepository).save(passwordSavedCaptor.capture());
        PasswordDBO saved = passwordSavedCaptor.getValue();

        assertEquals(model.getName(), saved.getName());
        assertEquals(model.getDescription(), saved.getDescription());
        assertEquals(model.getEmail_user(), saved.getEmail_user());
    }

    @Test
    void testCreatePasswordRepositoryException() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID user_id = UUID.randomUUID();

        when(this.aesUtil.encrypt(anyString())).thenReturn("passwordEncrypted");
        when(this.passwordRepository.save(any(PasswordDBO.class))).thenReturn(PasswordDataProvider.getPasswordDBO());

        when(this.userAdapter.getUserById(any(UUID.class))).thenReturn(UserDataProvider.getUserModel());
        when(this.userRepository.save(any(UserDBO.class))).thenThrow(IllegalArgumentException.class);

        // When + Then
        assertThrows(RuntimeException.class, () -> {
            passwordAdapter.createPassword(password, user_id);
        });
    }

    @Test
    void testCreatePasswordUserNotFound() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID user_id = UUID.randomUUID();

        ArgumentCaptor<PasswordDBO> passwordSavedCaptor = ArgumentCaptor.forClass(PasswordDBO.class);

        // When
        when(this.aesUtil.encrypt(anyString())).thenReturn("passwordEncrypted");
        when(this.userAdapter.getUserById(any(UUID.class))).thenThrow(new IllegalArgumentException("User with id " + user_id + " not found"));


        // THen
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.passwordAdapter.createPassword(password, user_id);
        });

        assertEquals("User with id " + user_id + " not found", exception.getMessage());
    }

    @Test
    void testEditPasswordSuccessfully() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID password_id = UUID.randomUUID();

        ArgumentCaptor<PasswordDBO> passwordSavedCaptor = ArgumentCaptor.forClass(PasswordDBO.class);

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.of(PasswordDataProvider.getPasswordDBO()));
        when(this.aesUtil.encrypt(anyString())).thenReturn("passwordEncrypted");
        when(this.passwordRepository.save(any(PasswordDBO.class))).thenReturn(PasswordDataProvider.getPasswordDBO());

        PasswordModel updated = this.passwordAdapter.editPassword(password, password_id);

        // Then
        assertNotNull(updated);

        verify(this.passwordRepository).save(passwordSavedCaptor.capture());
        PasswordDBO saved = passwordSavedCaptor.getValue();

        assertEquals("Name", saved.getName());
        assertEquals("passwordEncrypted", saved.getPassword());
        assertEquals("http://localhost:3306.com", saved.getUrl());
        assertEquals("test@test.com", saved.getEmail_user());
        assertEquals("passwordEncrypted", saved.getPassword());
        assertEquals("Description", saved.getDescription());

        assertEquals("Title", saved.getId_folder().getName());
    }

    @Test
    void testEditPasswordSuccessfullyWithoutFolder() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID password_id = UUID.randomUUID();

        password.setId_folder(null);

        ArgumentCaptor<PasswordDBO> passwordSavedCaptor = ArgumentCaptor.forClass(PasswordDBO.class);

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.of(PasswordDataProvider.getPasswordDBO()));
        when(this.aesUtil.encrypt(anyString())).thenReturn("passwordEncrypted");
        when(this.passwordRepository.save(any(PasswordDBO.class))).thenReturn(PasswordDataProvider.getPasswordDBO());

        PasswordModel updated = this.passwordAdapter.editPassword(password, password_id);

        // Then
        assertNotNull(updated);

        verify(this.passwordRepository).save(passwordSavedCaptor.capture());
        PasswordDBO saved = passwordSavedCaptor.getValue();

        assertEquals("Name", saved.getName());
        assertEquals("passwordEncrypted", saved.getPassword());
        assertEquals("http://localhost:3306.com", saved.getUrl());
        assertEquals("test@test.com", saved.getEmail_user());
        assertEquals("passwordEncrypted", saved.getPassword());
        assertEquals("Description", saved.getDescription());
    }

    @Test
    void testEditPasswordNotFound(){
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID password_id = UUID.randomUUID();

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.empty());


        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.passwordAdapter.editPassword(password, password_id);
        });

        assertEquals("Password not found", exception.getMessage());
    }

    @Test
    void testEditPasswordAESException() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Given
        PasswordModel password = PasswordDataProvider.getPasswordModel();
        UUID password_id = UUID.randomUUID();

        ArgumentCaptor<PasswordDBO> passwordSavedCaptor = ArgumentCaptor.forClass(PasswordDBO.class);

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.of(PasswordDataProvider.getPasswordDBO()));
        when(this.aesUtil.encrypt(anyString())).thenThrow(new RuntimeException());


        // Then
        assertThrows(RuntimeException.class, () -> {
            this.passwordAdapter.editPassword(password, password_id);
        });
    }

    @Test
    void testDeletePasswordSuccessfully(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.of(PasswordDataProvider.getPasswordDBO()));
        doNothing().when(this.passwordRepository).delete(any(PasswordDBO.class));

        String result = this.passwordAdapter.deletePassword(id);

        // Then
        assertNotNull(result);

        assertEquals("Password with id " + id + " removed", result);
    }

    @Test
    void testDeletePasswordNotFound(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.passwordRepository.findById(any(UUID.class))).thenReturn(Optional.empty());


        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.passwordAdapter.deletePassword(id);
        });

        assertEquals("Password not found", exception.getMessage());
    }
}
