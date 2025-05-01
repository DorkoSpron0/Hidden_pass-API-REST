package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.UserLoginModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.ResetMasterPasswordDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UpdateMasterPassword;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UpdateUserDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;
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
                .andExpect(jsonPath("$.url_image").value("http://image.co"))
                .andExpect(jsonPath("$.noteList[0].title").value("titleNote"))
                .andExpect(jsonPath("$.passwordList[0].name").value("passwordname"))
                .andExpect(jsonPath("$.folderList[0].name").value("folderName"));
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
                            "userName": "username",
                            "gmail": "nicky@nicky.com",
                            "masterPassword": "MasterPasswor@12d",
                            "urImage": "http://image.co"
                        }
                        """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginUser() throws Exception {
        // Given
        UserDTO dto = UserDataProvider.getUserDTO();

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
        UpdateUserDTO userDTO = new UpdateUserDTO(new UsernameValueObject("Nicky"), new EmailValueObject("test@test.com"), "http://image.ico");

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
        UpdateUserDTO userDTO = new UpdateUserDTO(new UsernameValueObject("Nicky"), new EmailValueObject("test@test.com"), "http://image.ico");

        // When
        when(this.userUseCases.updateUser(any(UUID.class), any(UserModel.class))).thenThrow(new IllegalArgumentException("User with email " + userDTO.getEmail() + " not found"));

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
                .andExpect(content().string("User with email " + userDTO.getEmail() + " not found"));
    }

    @Test
    void testRecoverMasterPassword() throws Exception {
        // Given
        String inputEmail = "y2kdsp@gmail.com";
        String newPassword = "Password@123";
        ResetMasterPasswordDTO passwordDTO = new ResetMasterPasswordDTO(inputEmail, newPassword);

        UserModel expectedUser = new UserModel();
        expectedUser.setEmail(new EmailValueObject(inputEmail)); // asegúrate de que el UserModel mock devuelva el mismo email del input

        when(userUseCases.recoverMasterPassword(eq(newPassword), any(EmailValueObject.class)))
                .thenReturn(expectedUser);

        // When / Then
        mockMvc.perform(put("/api/v1/hidden_pass/users/update/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email": "y2kdsp@gmail.com",
                        "new_password": "Password@123"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(inputEmail));

        // Verify
        verify(userUseCases).recoverMasterPassword(eq(newPassword), any(EmailValueObject.class));
    }
    @Test
    void testRecoverMasterPasswordNotFound() throws Exception {
        // Given
        String inputEmail = "y2kdsp@gmail.com";
        String newPassword = "Password@123";
        ResetMasterPasswordDTO passwordDTO = new ResetMasterPasswordDTO(inputEmail, newPassword);

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
        UpdateMasterPassword masterPassword = new UpdateMasterPassword(currentPassword, newPassword);

        UserModel userExpected = new UserModel();
        userExpected.setMaster_password(newPassword);

        // When
        when(this.userUseCases.updateMasterPassword(eq(id), eq(currentPassword), eq(newPassword))).thenReturn(userExpected);

        // Then
        mockMvc.perform(put("/api/v1/hidden_pass/users/update/password/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "current_password": "Password123@",
                            "new_password": "Test1235434@"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.master_password").value(newPassword));

        verify(this.userUseCases).updateMasterPassword(eq(id), eq(currentPassword), eq(newPassword));
    }
}