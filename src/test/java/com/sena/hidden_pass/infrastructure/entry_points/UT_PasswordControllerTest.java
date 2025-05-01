package com.sena.hidden_pass.infrastructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
