package com.sena.hidden_pass.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.MasterPasswordValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;
import com.sena.hidden_pass.infrastructure.entry_points.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private String token;

    @Mock
    private UserUseCases userUseCases;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void registerUser() throws Exception {
        // Arrange
        UserDTO userDTO = UserDTO.builder()
                .username(new UsernameValueObject("username"))
                .email(new EmailValueObject("test@test.com"))
                .master_password(new MasterPasswordValueObject("Password@123"))
                .build();

        String jsonPayload = objectMapper.writeValueAsString(userDTO);
        System.out.println("JSON Payload: " + jsonPayload);


        when(userUseCases.registerUser(any())).thenReturn(userDTO.toDomain());

        try {
            mockMvc.perform(post("/api/v1/hidden_pass/users/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    //.andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginUser() throws Exception{
        UserDTO userDTO = UserDTO.builder()
                .username(new UsernameValueObject("username"))
                .email(new EmailValueObject("test@test.com"))
                .master_password(new MasterPasswordValueObject("Password@123"))
                .build();

        String jsonPayload = objectMapper.writeValueAsString(userDTO);

        when(userUseCases.loginUser(any())).thenReturn("");

        try {
            MvcResult result = mockMvc.perform(post("/api/v1/hidden_pass/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload))
                    //.andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            // Extraer el cuerpo de la respuesta JSON

            this.token = result.getResponse().getContentAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
