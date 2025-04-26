package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.ISecurityCodesRepository;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UT_ISecurityCodesAdapterTest {

    @Mock
    private IUserAdapter userAdapter;

    @Mock
    private ISecurityCodesRepository securityCodesRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ISecurityCodesAdapter securityCodesAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void clean(){
        reset(securityCodesRepository, userRepository, mailService, userAdapter);
    }

    @Test
    void testSendSecurityCodeSuccessfully() throws MessagingException {
        // Given
        String email = "test@test.com";
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // When
        when(this.userAdapter.getUserByUEmail(anyString())).thenReturn(UserDataProvider.getUserModel());
        when(this.securityCodesRepository.save(any(SecurityCodesDBO.class))).thenReturn(new SecurityCodesDBO(UUID.randomUUID()));
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());

        doNothing().when(this.mailService).sendEmailAyncImpl(anyString(), anyString(), anyString());

        String message = this.securityCodesAdapter.sendSecurityCode(email);

        // then
        assertNotNull(message);

        assertEquals("El código de seguridad fue enviado al correo: " + email +  " revisa tu bandeja de entrada.", message);

        verify(this.mailService).sendEmailAyncImpl(captor.capture(), anyString(), anyString());
        String emailCapture = captor.getValue();

        assertEquals(emailCapture, email);

        verify(this.securityCodesRepository).save(any(SecurityCodesDBO.class));
    }

    @Test
    void testSendSecurityCodeNotFound(){
        // Given
        String email = "test@test.com";

        // When
        when(this.userAdapter.getUserByUEmail(anyString()))
                .thenThrow(new UsernameNotFoundException("User with email " + email + " not found"));

        // then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,() -> {
            this.securityCodesAdapter.sendSecurityCode(email);
        });

        assertEquals("User with email " + email + " not found", exception.getMessage());
    }

    @Test
    void testValidateSecurityCodeSuccessfully(){
        // Given
        UUID code = UUID.randomUUID();
        String email = "nicky@nicky.com";

        UserModel model = UserDataProvider.getUserModel();
        model.setSecurityCodes(new SecurityCodesModel(code));

        // When
        when(this.userAdapter.getUserByUEmail(anyString())).thenReturn(model);
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(UserDataProvider.getUserDBO());
        boolean result = this.securityCodesAdapter.validateSecurityCode(code, email);

        // Then
        assertTrue(result);

        verify(this.userRepository).save(any(UserDBO.class));
    }

    @Test
    void testValidateSecurityCodeNotFound(){
        // Given
        UUID code = UUID.randomUUID();
        String email = "nicky@nicky.com";

        UserModel model = UserDataProvider.getUserModel();
        model.setSecurityCodes(new SecurityCodesModel(code));

        // When
        when(this.userAdapter.getUserByUEmail(anyString())).thenThrow(new UsernameNotFoundException("User with email " + email + " not found"));

        // Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.securityCodesAdapter.validateSecurityCode(code, email);
        });

        assertEquals("User with email " + email + " not found", exception.getMessage());
    }

    @Test
    void testValidateSecurityCodeDoesntHaveSecurityCode(){
        // Given
        UUID code = UUID.randomUUID();
        String email = "nicky@nicky.com";

        UserModel model = UserDataProvider.getUserModel();
        model.setSecurityCodes(null);

        // When
        when(this.userAdapter.getUserByUEmail(anyString())).thenReturn(model);

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.securityCodesAdapter.validateSecurityCode(code, email);
        });

        assertEquals("User dont Have security code", exception.getMessage());
    }

    @Test
    void testValidateSecurityCodeDoesntMatch(){
        // Given
        UUID code = UUID.randomUUID();
        String email = "nicky@nicky.com";

        UserModel model = UserDataProvider.getUserModel();
        model.setSecurityCodes(new SecurityCodesModel(code));

        // When
        when(this.userAdapter.getUserByUEmail(anyString())).thenReturn(UserDataProvider.getUserModel());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.securityCodesAdapter.validateSecurityCode(UUID.randomUUID(), email);
        });

        assertEquals("Security Code invalid", exception.getMessage());
    }

    @Test
    void testGetSecurityCode() {
        // When
        SecurityCodesModel codesModel = this.securityCodesAdapter.getSecurityCode();

        // Then
        assertNull(codesModel);
    }
}
