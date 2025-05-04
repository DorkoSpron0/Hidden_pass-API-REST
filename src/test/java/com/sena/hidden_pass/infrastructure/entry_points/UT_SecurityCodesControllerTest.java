package com.sena.hidden_pass.infrastructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.usecases.SecurityCodesCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.SendSecurityCodeRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.ValidateSecurityCodeRequestDTO;
import jakarta.mail.MessagingException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityCodesController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class UT_SecurityCodesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityCodesCases securityCodesCases;

    @TestConfiguration
    static class MockConfig{
        @Bean
        public SecurityCodesCases securityCodesCases(){
            return Mockito.mock(SecurityCodesCases.class);
        }
    }

    @AfterEach
    void cleanMocks(){
        Mockito.reset(securityCodesCases);
    }

    @Test
    void testSendSecurityCode() throws Exception {
        // given
        SendSecurityCodeRequestDTO sendSecurityCodeDTO = new SendSecurityCodeRequestDTO("test@test.com");

        // When
        when(this.securityCodesCases.sendSecurityCode(eq(sendSecurityCodeDTO.email()))).thenReturn("Email sent successfully");

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/codes/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendSecurityCodeDTO))
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully"));

        verify(this.securityCodesCases).sendSecurityCode(eq(sendSecurityCodeDTO.email()));
    }

    @Test
    void testSendSecurityCodeMessagingException() throws Exception {
        // given
        SendSecurityCodeRequestDTO sendSecurityCodeDTO = new SendSecurityCodeRequestDTO("test@test.com");

        // When
        when(this.securityCodesCases.sendSecurityCode(anyString())).thenThrow(new MessagingException("Error sending the email"));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/codes/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendSecurityCodeDTO))
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error sending the email"));

        verify(this.securityCodesCases).sendSecurityCode(eq(sendSecurityCodeDTO.email()));
    }

    @Test
    void testValidateSecurityCode() throws Exception {
        // Given
        ValidateSecurityCodeRequestDTO validateSecurityCodeDTO = new ValidateSecurityCodeRequestDTO("test@test.com", UUID.randomUUID().toString());

        // When
        when(this.securityCodesCases.validateSecurityCode(any(UUID.class), anyString())).thenReturn(true);

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/codes/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validateSecurityCodeDTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("VALID"));
    }

    @Test
    void testValidateSecurityCodeNotValid() throws Exception {
        // Given
        ValidateSecurityCodeRequestDTO validateSecurityCodeDTO = new ValidateSecurityCodeRequestDTO("test@test.com", UUID.randomUUID().toString());

        // When
        when(this.securityCodesCases.validateSecurityCode(any(UUID.class), anyString())).thenReturn(false);

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/codes/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validateSecurityCodeDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NO VALID"));
    }

    @Test
    void testValidateSecurityCodeNotFound() throws Exception {
        // Given
        ValidateSecurityCodeRequestDTO validateSecurityCodeDTO = new ValidateSecurityCodeRequestDTO("test@test.com", UUID.randomUUID().toString());

        // When
        when(this.securityCodesCases.validateSecurityCode(any(UUID.class), anyString())).thenThrow(new UsernameNotFoundException("User not found"));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/codes/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validateSecurityCodeDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

}
