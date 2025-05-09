package com.sena.hidden_pass.infrastructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.UserLoginModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class UT_UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserUseCases userUseCases;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserUseCases userUseCases() {
            return Mockito.mock(UserUseCases.class);
        }
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(userUseCases);  // o cualquier otro mock
    }

    @Test
    void testGetUserById() throws Exception {
        UUID id = UUID.randomUUID();
        when(userUseCases.getUserById(any(UUID.class)))
                .thenReturn(UserDataProvider.getUserModel());

        mockMvc.perform(get("/api/v1/hidden_pass/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nicky@nicky.com"))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.url_image").value("http://image.co"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(userUseCases.getUserById(any(UUID.class)))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(get("/api/v1/hidden_pass/users/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testRegisterUser() throws Exception {
        // Given

        // When
        when(this.userUseCases.registerUser(any(UserModel.class))).thenReturn(UserDataProvider.getUserModel());

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/users/register")
                .contentType("application/json")
                .content("""
                        {
                            "username": "username",
                            "email": "nicky@nicky.com",
                            "master_password": "MasterPasswor@12d",
                            "url_image": "http://image.co"
                        }
                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.email").value("nicky@nicky.com"))
                .andExpect(jsonPath("$.url_image").value("http://image.co"));
    }

    @Test
    void testRegisterUserBadRequest() throws Exception {
        // Given

        // When
        when(this.userUseCases.registerUser(any(UserModel.class))).thenReturn(UserDataProvider.getUserModel());

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/users/register")
                        .contentType("application/json")
                        .content("""
                        {
                            "urImage": "http://image.co"
                        }
                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginUser() throws Exception {
        // Given
        LoginUserRequestDTO dto = new LoginUserRequestDTO("email@email.com", "master_password");

        // When
        when(this.userUseCases.loginUser(any(UserModel.class))).thenReturn(new UserLoginModel(
                UUID.randomUUID(),
                new UsernameValueObject("username"),
                new EmailValueObject("email@email.com"),
                "jwtValidToken",
                "url"
        ));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/users/login")
                .contentType("application/json")
                .content("""
                        {
                            "email": "m@m.com",
                            "master_password": "Admin123@"
                        }
                        """)
        ).andExpect(jsonPath("$.token").value("jwtValidToken"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {

        EmailValueObject email = new EmailValueObject("test@test.com");
        // When
        when(this.userUseCases.loginUser(any(UserModel.class))).thenThrow(new UsernameNotFoundException("User with email " + email + " not found"));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/users/login")
                .contentType("application/json")
                .content("""
                        {
                            "email": "m@m.com",
                            "master_password": "Admin123@"
                        }
                        """)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with email " + email + " not found"));
    }

    @Test
    void testLoginUserIncorrectCredentials() throws Exception {

        EmailValueObject email = new EmailValueObject("test@test.com");
        // When
        when(this.userUseCases.loginUser(any(UserModel.class))).thenThrow(new IllegalArgumentException("Credenciales incorrectas"));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/users/login")
                        .contentType("application/json")
                        .content("""
                        {
                            "email": "m@m.com",
                            "master_password": "Admin123@"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Credenciales incorrectas"));
    }

    @Test
    void testUpdateUser() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.userUseCases.updateUser(any(UUID.class), any(UserModel.class))).thenReturn(UserDataProvider.getUserModel());

        mockMvc.perform(put("/api/v1/hidden_pass/users/update/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "username",
                            "email": "nicky@nicky.com",
                            "master_password_saved": "passwordSaved",
                            "url_image": "http://image.co"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.email").value("nicky@nicky.com"))
                .andExpect(jsonPath("$.url_image").value("http://image.co"));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UpdateUserRequestDTO userDTO = new UpdateUserRequestDTO("Nicky", "test@test.com", "http://image.ico");

        // When
        when(this.userUseCases.updateUser(any(UUID.class), any(UserModel.class))).thenThrow(new IllegalArgumentException("User with email " + userDTO.email() + " not found"));

        mockMvc.perform(put("/api/v1/hidden_pass/users/update/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "username",
                            "email": "nicky@nicky.com",
                            "master_password_saved": "passwordSaved",
                            "url_image": "http://image.co"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with email " + userDTO.email() + " not found"));
    }

    @Test
    void testRecoverMasterPassword() throws Exception {
        // given
        ResetMasterPasswordRequestDTO dto = new ResetMasterPasswordRequestDTO("y2kdsp@gmail.com", "Password123@");

        // When
        when(this.userUseCases.recoverMasterPassword(dto.new_password(), new EmailValueObject(dto.email()))).thenReturn(new UserModel(
                UUID.randomUUID(),
                new EmailValueObject(dto.email()),
                new UsernameValueObject("Username"),
                dto.new_password(),
                "url",
                null,
                null,
                null,
                null
        ));

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/users/update/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(dto.email()));
    }

    @Test
    void testRecoverMasterPasswordNotFound() throws Exception {
        // Given
        String inputEmail = "y2kdsp@gmail.com";
        String newPassword = "Password@123";
        ResetMasterPasswordRequestDTO passwordDTO = new ResetMasterPasswordRequestDTO(inputEmail, newPassword);

        UserModel expectedUser = new UserModel();
        expectedUser.setEmail(new EmailValueObject(inputEmail)); // asegúrate de que el UserModel mock devuelva el mismo email del input

        when(userUseCases.recoverMasterPassword(eq(newPassword), any(EmailValueObject.class)))
                .thenThrow(new UsernameNotFoundException("User with email " + inputEmail + " not found"));

        // When / Then
        mockMvc.perform(put("/api/v1/hidden_pass/users/update/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email": "y2kdsp@gmail.com",
                        "new_password": "Password@123"
                    }
                    """))
                .andExpect(status().isBadRequest())
                        .andExpect(content().string("User with email " + inputEmail + " not found"));

        // Verify
        verify(userUseCases).recoverMasterPassword(eq(newPassword), any(EmailValueObject.class));
    }

    @Test
    void testUpdateMasterPassword() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        String currentPassword = "Password123@";
        String newPassword = "Test1235434@";

        UpdateMasterPasswordRequestDTO masterPassword = new UpdateMasterPasswordRequestDTO(currentPassword, newPassword);

        // When
        when(this.userUseCases.updateMasterPassword(eq(id), eq(currentPassword), eq(newPassword))).thenReturn(new UserModel(
                id,
                new EmailValueObject("m@m.com"),
                new UsernameValueObject("username"),
                newPassword,
                "url",
                null,
                null,
                null,
                null
        ));

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/users/update/password/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(masterPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.master_password").value(newPassword));

        verify(this.userUseCases).updateMasterPassword(eq(id), eq(currentPassword), eq(newPassword));
    }

    @Test
    void testDeleteUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        DeleteUserRequestDTO deleteUserDTO = new DeleteUserRequestDTO("current_password");

        // When
        when(this.userUseCases.deleteUser(eq(userId), anyString())).thenReturn("User deleted successfully");

        // Then
        mockMvc.perform(delete("/api/v1/hidden_pass/users/delete/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteUserDTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(this.userUseCases).deleteUser(eq(userId), anyString());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        DeleteUserRequestDTO deleteUserDTO = new DeleteUserRequestDTO("current_password");

        // When
        when(this.userUseCases.deleteUser(eq(userId), anyString())).thenThrow(new IllegalArgumentException("User not found"));

        // Then
        mockMvc.perform(delete("/api/v1/hidden_pass/users/delete/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteUserDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }
}