package com.sena.hidden_pass.infrastructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.PasswordRequestDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PasswordController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class UT_PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordUseCases passwordUseCases;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfigure {
        @Bean
        public PasswordUseCases passwordUseCases(){
            return Mockito.mock(PasswordUseCases.class);
        }
    }

    @Test
    void testGetPasswords() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        PasswordModel modelExpected = new PasswordModel();
        modelExpected.setPassword("password");
        modelExpected.setName("namePassword");
        modelExpected.setEmail_user("test@test.com");

        // When
        when(this.passwordUseCases.getAllPassword(eq(userId))).thenReturn(Set.of(modelExpected));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/passwords/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("namePassword"))
                .andExpect(jsonPath("$[0].password").value("password"))
                .andExpect(jsonPath("$[0].email_user").value("test@test.com"));

        verify(this.passwordUseCases).getAllPassword(eq(userId));
    }

    @Test
    void testGetPasswordsNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.passwordUseCases.getAllPassword(eq(userId))).thenThrow(new IllegalArgumentException("Password not found"));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/passwords/{id}", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password not found"));
    }

    @Test
    void testGetPasswordById() throws Exception {
        // Given
        UUID passwordId = UUID.randomUUID();
        PasswordModel modelExpected = new PasswordModel();
        modelExpected.setPassword("password");
        modelExpected.setName("namePassword");
        modelExpected.setEmail_user("test@test.com");

        // When
        when(this.passwordUseCases.getPasswordById(eq(passwordId))).thenReturn(modelExpected);

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/passwords/password/{id}", passwordId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("namePassword"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.email_user").value("test@test.com"));

        verify(this.passwordUseCases).getPasswordById(eq(passwordId));
    }

    @Test
    void testGetPasswordByIdNotFound() throws Exception {
        // Given
        UUID passwordId = UUID.randomUUID();
        PasswordModel modelExpected = new PasswordModel();
        modelExpected.setPassword("password");
        modelExpected.setName("namePassword");
        modelExpected.setEmail_user("test@test.com");

        // When
        when(this.passwordUseCases.getPasswordById(eq(passwordId))).thenThrow(new IllegalArgumentException("Password not found"));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/passwords/password/{id}", passwordId))
                .andExpect(status().isBadRequest())
                        .andExpect(content().string("Password not found"));
    }

    @Test
    void testCreatePassword() throws Exception{
        // Given
        PasswordRequestDTO password = new PasswordRequestDTO(
                "Name",
                "HttP::Localhost.com",
                LocalDateTime.now(),
                "test@test.com",
                "Passwird",
                "description",
                "Name");

        UUID userId = UUID.randomUUID();
        PasswordModel modelExpected = new PasswordModel();
        modelExpected.setPassword(password.password());
        modelExpected.setName(password.name());
        modelExpected.setEmail_user(password.email_user());
        modelExpected.setId_password(UUID.randomUUID());

        // When
        when(this.passwordUseCases.createPassword(any(), eq(userId), any())).thenReturn(modelExpected);

        // THen
        mockMvc.perform(post("/api/v1/hidden_pass/passwords/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(password))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(password.name()))
                .andExpect(jsonPath("$.password").value(password.password()))
                .andExpect(jsonPath("$.email_user").value(password.email_user()));
    }

    @Test
    void testUpdatePassword() throws Exception {
         // Given
        PasswordRequestDTO password = new PasswordRequestDTO("Name",
                "HttP::Localhost.com",
                LocalDateTime.now(),
                "test@test.com",
                "Passwird",
                "description",
                "Name");

        UUID passwordId = UUID.randomUUID();

        PasswordModel modelExpected = new PasswordModel();
        modelExpected.setPassword("Passwird");
        modelExpected.setName("Name");
        modelExpected.setEmail_user("test@test.com");

        ArgumentCaptor<PasswordModel> captor = ArgumentCaptor.forClass(PasswordModel.class);

        // When
        when(this.passwordUseCases.editPassword(any(PasswordModel.class), eq(passwordId), any())).thenReturn(modelExpected);

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/passwords/password/{id}", passwordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(password))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(password.name()))
                .andExpect(jsonPath("$.password").value(password.password()))
                .andExpect(jsonPath("$.email_user").value(password.email_user()));

        verify(this.passwordUseCases).editPassword(any(PasswordModel.class), eq(passwordId), any());

        verify(passwordUseCases).editPassword(captor.capture(), eq(passwordId), any());
        PasswordModel captured = captor.getValue();
        assertEquals("Name", captured.getName());
        assertEquals("test@test.com", captured.getEmail_user());
    }

    @Test
    void testUpdatePasswordNotFound() throws Exception {
        // Given
        PasswordRequestDTO password = new PasswordRequestDTO("Name",
                "HttP::Localhost.com",
                LocalDateTime.now(),
                "test@test.com",
                "Passwird",
                "description",
                "Name");

        UUID passwordId = UUID.randomUUID();

        // When
        when(this.passwordUseCases.editPassword(any(PasswordModel.class), eq(passwordId), any())).thenThrow(new IllegalArgumentException("User not found"));

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/passwords/password/{id}", passwordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(password))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testDeletePassword() throws Exception {
        // Given
        UUID passwordId = UUID.randomUUID();

        // When
        when(this.passwordUseCases.deletePassword(eq(passwordId))).thenReturn("Password deleted successfully");

        // THen
        mockMvc.perform(delete("/api/v1/hidden_pass/passwords/password/{id}", passwordId))
                .andExpect(status().isOk())
                .andExpect(content().string("Password deleted successfully"));

        verify(this.passwordUseCases).deletePassword(eq(passwordId));
    }

    @Test
    void testDeletePasswordNotFound() throws Exception {
        // Given
        UUID passwordId = UUID.randomUUID();

        // When
        when(this.passwordUseCases.deletePassword(eq(passwordId))).thenThrow(new IllegalArgumentException("Password not found"));

        // THen
        mockMvc.perform(delete("/api/v1/hidden_pass/passwords/password/{id}", passwordId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password not found"));
    }

}
