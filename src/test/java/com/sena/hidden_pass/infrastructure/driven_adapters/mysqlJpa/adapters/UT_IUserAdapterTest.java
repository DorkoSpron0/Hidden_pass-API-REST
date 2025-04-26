package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.services.MailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UT_IUserAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @InjectMocks
    private IUserAdapter userAdapter;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void clean(){
        reset(mailService, passwordEncoder, jwtFilter, userRepository);
    }

    // TODO - register fail when value objects failed
    @Test
    public void testRegisterUser() throws MessagingException {
        // Given
        UserModel given = UserDataProvider.getUserModel();

        UserDBO dboWithEncodedPassword = UserDataProvider.getUserDBO();
        dboWithEncodedPassword.setMaster_password("password_cifrada");

        // When
        doNothing().when(mailService).sendEmailAyncImpl(anyString(), anyString(), anyString());
        when(passwordEncoder.encode(anyString())).thenReturn("password_cifrada");
        when(userRepository.save(any(UserDBO.class))).thenReturn(
                dboWithEncodedPassword
        );

        UserModel model = this.userAdapter.registerUser(given);

        // Then
        assertNotNull(model);

        assertEquals(given.getUsername().getUsername(), model.getUsername().getUsername());
        assertEquals("password_cifrada", model.getMaster_password());
        assertEquals(given.getUrl_image(), model.getUrl_image());

        verify(mailService).sendEmailAyncImpl(anyString(), anyString(), anyString());
        verify(userRepository).save(any(UserDBO.class));
        verify(passwordEncoder).encode(anyString());

    }

    @Test
    public void testRegisterUserException() throws MessagingException {
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        doThrow(new RuntimeException()).when(mailService).sendEmailAyncImpl(anyString(), anyString(), anyString());

        // Then
        assertThrows(RuntimeException.class, () -> {
            this.userAdapter.registerUser(model);
        });
    }

    @Test
    public void testGetUserByIdSuccessfully(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        UserModel result = this.userAdapter.getUserById(id);

        // Then
        assertNotNull(result);

        assertEquals("username", result.getUsername().getUsername());
        assertEquals("nicky@nicky.com", result.getEmail().getEmail());
        assertEquals("master_password", result.getMaster_password());

        verify(this.userRepository).findById(any(UUID.class));
    }

    @Test
    public void testGetUserByIdNotFound(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class, () -> {
            this.userAdapter.getUserById(id);
        });

        assertEquals("User with id " + id + " not found", argumentException.getMessage());
    }

    @Test
    public void testGetUserByUsernameSuccessfully(){
        // Given
        String username = "username";

        // When
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        UserModel result = this.userAdapter.getUserByUsername(username);

        // Then
        assertNotNull(result);

        assertEquals(username, result.getUsername().getUsername());
        assertEquals("nicky@nicky.com", result.getEmail().getEmail());
        assertEquals("master_password", result.getMaster_password());

        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testGetUserByUsernameNotFound(){
        // Given
        String username = "username";

        // When
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Then
        UsernameNotFoundException argumentException = assertThrows(UsernameNotFoundException.class, () -> {
            this.userAdapter.getUserByUsername(username);
        });

        assertEquals("User with username " + username + " not found", argumentException.getMessage());
    }

    @Test
    void testGetUserByUEmailSuccessfully(){
        // Given
        String email = "nicky@nicky.com";

        // When
        when(this.userRepository.findByEmail(any(EmailValueObject.class))).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        UserModel result = this.userAdapter.getUserByUEmail(email);

        // Then
        assertNotNull(result);

        assertEquals("username", result.getUsername().getUsername());
        assertEquals(email, result.getEmail().getEmail());
        assertEquals("master_password", result.getMaster_password());

        verify(this.userRepository).findByEmail(any(EmailValueObject.class));
    }

    @Test
    void testGetUserByUEmailNotFound(){
        // Given
        String email = "nicky@nicky.com";

        // When
        when(this.userRepository.findByEmail(any(EmailValueObject.class))).thenReturn(Optional.empty());

        // Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.userAdapter.getUserByUEmail(email);
        });

        assertEquals("User with email " + email + " not found", exception.getMessage());
    }

    @Test
    void testLoginUserSuccessfully(){
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        when(this.userRepository.findByEmail_Email(anyString())).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(this.jwtFilter.generateToken(any(UUID.class))).thenReturn("faketoken"); // 👈 mock agregado

        String token = this.userAdapter.loginUser(model);

        // Then
        assertNotNull(token);

        assertEquals("faketoken", token);
        verify(this.userRepository).findByEmail_Email(anyString());
        verify(this.passwordEncoder).matches(anyString(), anyString());
        verify(this.jwtFilter).generateToken(any());
    }

    @Test
    void testLoginUserNotFound(){
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        when(this.userRepository.findByEmail_Email(anyString())).thenReturn(Optional.empty());

        // Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.userAdapter.loginUser(model);
        });

        assertEquals("User with email " + model.getEmail() + " not found", exception.getMessage());
    }

    @Test
    void testLoginUserPasswordDoesntMatch(){
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        when(this.userRepository.findByEmail_Email(anyString())).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userAdapter.loginUser(model);
        });

        assertEquals("Credenciales incorrectas", exception.getMessage());
        verify(this.userRepository).findByEmail_Email(anyString());
        verify(this.passwordEncoder).matches(anyString(), anyString());
    }

    @Test
    void testUpdateUserSuccessfully(){
        // Given
        UUID id = UUID.randomUUID();
        UserModel model = UserDataProvider.getUserModel();
        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);

        //when
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(
                new UserDBO(id, new UsernameValueObject("user"), new EmailValueObject("test@test.com"), "master_password", "url:http.com",
                        null, null, null, null
        ));
        UserModel updated = this.userAdapter.updateUser(id, model);

        // Then
        assertNotNull(updated);

        assertEquals(id, updated.getId_usuario());
        assertEquals("user", updated.getUsername().getUsername());
        assertEquals("test@test.com", updated.getEmail().getEmail());
        assertEquals("master_password", updated.getMaster_password());

        verify(this.userRepository).save(captor.capture());
        UserDBO saved = captor.getValue();

        assertEquals(model.getUsername().getUsername(), saved.getUsername().getUsername());
        assertEquals(model.getEmail().getEmail(), saved.getEmail().getEmail());
    }

    @Test
    void testUpdateUserNotFound(){
        // Given
        UUID id = UUID.randomUUID();
        UserModel model = UserDataProvider.getUserModel();
        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);

        //when
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userAdapter.updateUser(id, model);
        });

        assertEquals("User with id " + id + " not found", exception.getMessage());
    }

    @Test // TODO - TDD
    void testDeleteUser(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        UserModel result = this.userAdapter.deleteUser(id);

        // Then
        assertNull(result);
    }

    @Test
    void testRecoverMasterPasswordSuccessfully(){
        // Given
        String password = "master_password";
        EmailValueObject email = new EmailValueObject("test@test.com");
        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);

        // When
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.passwordEncoder.encode(anyString())).thenReturn("passwordencrypted");
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());
        UserModel result = this.userAdapter.recoverMasterPassword(password, email);

        verify(this.userRepository).save(captor.capture());
        UserDBO saved = captor.getValue();

        // Then
        assertEquals("passwordencrypted", saved.getMaster_password());
        assertEquals("username", result.getUsername().getUsername());
        assertEquals("nicky@nicky.com", result.getEmail().getEmail());
    }

    @Test
    void testRecoverMasterPasswordNotFound(){
        // Given
        String password = "master_password";
        EmailValueObject email = new EmailValueObject("test@test.com");

        // When
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.userAdapter.recoverMasterPassword(password, email);
        });

        assertEquals("User with email " + email.getEmail() + " not found", exception.getMessage());
    }

    @Test
    void testUpdateMasterPasswordSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        String current_password = "Password123@";
        String new_password = "newPassword123@";
        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);

        // When
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(this.passwordEncoder.encode(anyString())).thenReturn("password_cifrada");
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());

        UserModel model = this.userAdapter.updateMasterPassword(id, current_password, new_password);

        // then
        assertNotNull(model);

        verify(this.userRepository).save(captor.capture());
        UserDBO saved = captor.getValue();

        assertEquals("password_cifrada", saved.getMaster_password());
        assertEquals("username", model.getUsername().getUsername());
        assertEquals("nicky@nicky.com", model.getEmail().getEmail());
        assertEquals("http://image.co", model.getUrl_image());
    }

    @Test
    void testUpdateMasterPasswordNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        String current_password = "Password123@";
        String new_password = "newPassword123@";

        // When
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userAdapter.updateMasterPassword(id, current_password, new_password);
        });

        assertEquals("User with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testUpdateMasterPasswordDoesntMatch() {
        // Given
        UUID id = UUID.randomUUID();
        String current_password = "Password123@";
        String new_password = "newPassword123@";

        // When
        when(this.userRepository.findById(any(UUID.class))).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.userAdapter.updateMasterPassword(id, current_password, new_password);
        });

        assertEquals("Credenciales incorrectas", exception.getMessage());
    }

    @Test
    void testMatchPassword(){
        // Given
        String rawPassword = "Password123@";
        String encodedPassword = "password_cifrada";

        // When
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        boolean result = this.userAdapter.matchPassword(rawPassword, encodedPassword);

        // Then
        assertTrue(result);
    }
}
