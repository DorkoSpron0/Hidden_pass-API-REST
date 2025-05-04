package com.sena.hidden_pass.infrastructure.entry_points;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.FolderUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.FolderRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.FolderInfoResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = FolderController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class UT_FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FolderUseCases folderUseCases;

    @InjectMocks
    private FolderController folderController;

    @TestConfiguration
    static class MockConfig{
        @Bean
        public FolderUseCases folderUseCases(){
            return Mockito.mock(FolderUseCases.class);
        }
    }

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(folderUseCases);  // o cualquier otro mock
    }

    @Test
    void testGetAllFolders() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UUID folderId = UUID.randomUUID();

        // When
        when(this.folderUseCases.getAllFolders(eq(userId))).thenReturn(List.of(new FolderModel(
                folderId,
                "foldername",
                "description",
                "icon"
        )));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/folders/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("foldername"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].icon").value("icon"));
    }

    @Test
    void testCreateFolder() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        FolderRequestDTO folderRequestDTO = new FolderRequestDTO(
                "folderName",
                "icon",
                "descriptionfolder",
                List.of("PasswordName1", "PasswordName1")
        );

        // When
        when(this.folderUseCases.createFolder(any(FolderModel.class), eq(userId), eq(folderRequestDTO.passwords()))).thenReturn(new FolderModel(
                null,
                "folderName",
                "descriptionfodler",
                "icon"
        ));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/folders/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(folderRequestDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("folderName"))
                .andExpect(jsonPath("$.description").value("descriptionfodler"))
                .andExpect(jsonPath("$.icon").value("icon"));
    }

    @Test
    void testGetFolderById() throws Exception {
        // Given
        UUID folderId = UUID.randomUUID();

        // When
        when(this.folderUseCases.getFolderById(eq(folderId))).thenReturn(new FolderModel(
                folderId,
                "nameFolder",
                "descriptionFolder",
                "icon",
                new UserModel(
                        null,
                        new EmailValueObject("email@email.com"),
                        new UsernameValueObject("username"),
                        "MasterPassword123@",
                        "url",
                        null,
                        null,
                        null,
                        null
                )
        ));

        // Then
        mockMvc.perform(get("/api/v1/hidden_pass/folders/folder/{folderId}", folderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("nameFolder"))
                .andExpect(jsonPath("$.description").value("descriptionFolder"))
                .andExpect(jsonPath("$.icon").value("icon"));
    }

    @Test
    void testSetPasswordToFolder() throws Exception {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID passwordId = UUID.randomUUID();

        // When
        when(this.folderUseCases.setPasswordToFolder(eq(folderId), eq(passwordId))).thenReturn(new FolderModel(
                "description",
                "icon",
                folderId,
                "name",
                null,
                List.of(new PasswordModel(
                        passwordId,
                        "passwordname",
                        "passwordDescription",
                        "email@email.com",
                        "Password123@",
                        "url",
                        LocalDateTime.now(),
                        null
                ))
        ));

        // Then
        mockMvc.perform(post("/api/v1/hidden_pass/folders/{folderId}/{passwordId}", folderId, passwordId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_folder").value(folderId.toString()))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.icon").value("icon"))
                .andExpect(jsonPath("$.passwordModels[0].id_password").value(passwordId.toString()))
                .andExpect(jsonPath("$.passwordModels[0].name").value("passwordname"))
                .andExpect(jsonPath("$.passwordModels[0].description").value("passwordDescription"));
    }

    @Test
    void testUpdateFolder() throws Exception {
        // Given
        UUID folderId = UUID.randomUUID();
        FolderRequestDTO folderRequestDTO = new FolderRequestDTO(
                "new_foldername",
                "new_icon",
                "new_description",
                null
        );

        // WHen
        when(this.folderUseCases.updateFolder(any(FolderModel.class), eq(folderId), eq(folderRequestDTO.passwords()))).thenReturn(new FolderModel(
                folderId,
                "new_foldername",
                "new_folderdescription",
                "new_icon"
        ));

        mockMvc.perform(put("/api/v1/hidden_pass/folders/{folderId}", folderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(folderRequestDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("new_foldername"))
                .andExpect(jsonPath("$.description").value("new_folderdescription"))
                .andExpect(jsonPath("$.icon").value("new_icon"));
    }

    @Test
    void testDeleteFolder() throws Exception {
        // Given
        UUID folderId = UUID.randomUUID();

        // WHen
        when(this.folderUseCases.deleteFolder(eq(folderId))).thenReturn("Deleted");

        // Then
        mockMvc.perform(delete("/api/v1/hidden_pass/folders/{folderId}", folderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    @Test
    void modelToDTO() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID passwordId = UUID.randomUUID();

        FolderModel model = new FolderModel(
                "description",
                "icon",
                folderId,
                "name",
                null,
                List.of(new PasswordModel(
                        passwordId,
                        "name",
                        "description",
                        "email@email.com",
                        "paassword133@",
                        "url",
                        LocalDateTime.now(),
                        new FolderModel(
                                folderId, null,null,null
                        )
                ))
        );

        // When
        FolderInfoResponseDTO response = this.folderController.modelToDTO(model);

        // then
        assertEquals(model.getName(), response.name());
        assertEquals(model.getIcon(), response.icon());
        assertEquals(model.getDescription(), response.description());
        assertEquals(model.getId_folder(), response.id_folder());
        assertEquals(model.getPasswordModels().getFirst().getId_folder().getId_folder(), response.passwordModels().getFirst().id_folder());
        assertEquals(model.getPasswordModels().getFirst().getId_password(), response.passwordModels().getFirst().id_password());
        assertEquals(model.getPasswordModels().getFirst().getName(), response.passwordModels().getFirst().name());
        assertEquals(model.getPasswordModels().getFirst().getDateTime(), response.passwordModels().getFirst().dateTime());
        assertEquals(model.getPasswordModels().getFirst().getPassword(), response.passwordModels().getFirst().password());

    }
    @Test
    void modelToDTOWithoutPasswords() {
        // Given
        UUID folderId = UUID.randomUUID();

        FolderModel model = new FolderModel(
                "description",
                "icon",
                folderId,
                "name",
                null,
                null
        );

        // When
        FolderInfoResponseDTO response = this.folderController.modelToDTO(model);

        // then
        assertEquals(model.getName(), response.name());
        assertEquals(model.getIcon(), response.icon());
        assertEquals(model.getDescription(), response.description());
        assertEquals(model.getId_folder(), response.id_folder());

        assertEquals(0, response.passwordModels().size());
    }

}
