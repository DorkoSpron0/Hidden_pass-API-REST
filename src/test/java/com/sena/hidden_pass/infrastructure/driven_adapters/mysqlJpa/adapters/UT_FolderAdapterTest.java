package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.PasswordDataProvider;
import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IFolderRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.FolderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UT_FolderAdapterTest {

    @Mock
    private IFolderRepository folderRepository;

    @Mock
    private IPasswordRepository passwordRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private FolderMapper folderMapper;

    @InjectMocks
    private IFolderAdapter folderAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFolders(){
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        List<FolderModel> folderModels = this.folderAdapter.getAllFolders(userId);

        System.out.println(folderModels);

        // Then
        assertNotNull(folderModels);
        assertEquals("folderName", folderModels.getFirst().getName());
        assertEquals("descriptionFolder", folderModels.getFirst().getDescription());
        assertEquals("icon.png", folderModels.getFirst().getIcon());
        assertEquals(1, folderModels.size());
    }

    @Test
    void testGetAllFoldersUserNotFound(){
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.getAllFolders(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetFolderById(){
        // Given
        UUID id = UUID.randomUUID();

        FolderDBO folderExpected = new FolderDBO();
        folderExpected.setId_folder(id);
        folderExpected.setName("name");
        folderExpected.setDescription("description");
        folderExpected.setUser(UserDataProvider.getUserDBO());

        // When
        when(this.folderRepository.findById(eq(id))).thenReturn(Optional.of(folderExpected));

        FolderModel folderFounded = this.folderAdapter.getFolderById(id);

        // THen
        assertNotNull(folderFounded);

        verify(this.folderRepository).findById(eq(id));

        assertEquals(id, folderFounded.getId_folder());
        assertEquals("name", folderFounded.getName());
        assertEquals("description", folderFounded.getDescription());
    }

    @Test
    void testGetFolderByIdNotFound(){
        // Given
        UUID id = UUID.randomUUID();

        FolderDBO folderExpected = new FolderDBO();
        folderExpected.setId_folder(id);
        folderExpected.setName("name");
        folderExpected.setDescription("description");

        // When
        when(this.folderRepository.findById(eq(id))).thenReturn(Optional.empty());

        // THen
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.getFolderById(id);
        });

        assertEquals("Folder with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testGetFolderByName() {
        // Given
        String name = "FolderName";

        // When
        when(this.folderRepository.findFolderDBOByName(eq(name))).thenReturn(Optional.of(new FolderDBO(
                null,
                "FolderName",
                null,
                null
        )));

        FolderModel model = this.folderAdapter.getFolderByName(name);

        // Then
        verify(this.folderRepository).findFolderDBOByName(eq(name));

        assertEquals(name, model.getName());
    }

    @Test
    void testGetFolderByNameNotFound() {
        // Given
        String name = "FolderName";

        // When
        when(this.folderRepository.findFolderDBOByName(eq(name))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.getFolderByName(name);
        });
        assertEquals("Folder with name " + name + " not found", exception.getMessage());
    }

    @Test
    void testCreateFolder() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        UserDBO userDBO = new UserDBO();
        userDBO.setEmail("test@example.com");
        userDBO.setUsername("username");

        UserModel userModel = new UserModel();
        userModel.setEmail(new EmailValueObject("test@example.com"));
        userModel.setUsername(new UsernameValueObject("username"));

        FolderModel model = new FolderModel("descriptionFolder", "icon", folderId, "folderName" , userModel, new ArrayList<>(List.of(PasswordDataProvider.getPasswordModel())));

        FolderDBO folderExpected = new FolderDBO(UUID.randomUUID(), "folderName", "icon", "descriptionFolder", userDBO, new ArrayList<>(List.of(PasswordDataProvider.getPasswordDBO())));

        UserDBO userExpected = UserDataProvider.getUserDBO();

        ArgumentCaptor<FolderDBO> folderDBOArgumentCaptor = ArgumentCaptor.forClass(FolderDBO.class);

        // When
        when(this.userRepository.findById(eq(userId))).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(folderExpected);
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(userExpected);
        when(this.passwordRepository.findPasswordDBOByNameIn(any(List.class))).thenReturn(List.of(new PasswordDBO(
                null,
                "PasswordName",
                null,
                null,
                null,
                null,
                null,
                null
        )));
        FolderModel result = this.folderAdapter.createFolder(model,userId, List.of("PasswordName"));

        // Then
        assertNotNull(result);

        verify(this.folderRepository).save(any(FolderDBO.class));
        verify(this.folderRepository).save(folderDBOArgumentCaptor.capture());
        FolderDBO folderSaved = folderDBOArgumentCaptor.getValue();

        assertEquals(folderId, folderSaved.getId_folder());

        assertEquals("Name", result.getPasswordModels().getFirst().getName());
    }

    @Test
    void testCreateFolderNotFound() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        FolderModel model = new FolderModel("descriptionFolder", "icon", folderId, "folderName" , new UserModel(), new ArrayList<>(List.of(PasswordDataProvider.getPasswordModel())));

        // When
        when(this.userRepository.findById(eq(userId))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.createFolder(model,userId, null);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateFolderWithoutPasswords() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        UserDBO userDBO = new UserDBO();
        userDBO.setEmail("test@example.com");
        userDBO.setUsername("username");

        UserModel userModel = new UserModel();
        userModel.setEmail(new EmailValueObject("test@example.com"));
        userModel.setUsername(new UsernameValueObject("username"));

        FolderModel model = new FolderModel("descriptionFolder", "icon", folderId, "folderName" , userModel, new ArrayList<>(List.of(PasswordDataProvider.getPasswordModel())));

        FolderDBO folderExpected = new FolderDBO(UUID.randomUUID(), "folderName", "icon", "descriptionFolder", userDBO, new ArrayList<>(List.of(PasswordDataProvider.getPasswordDBO())));

        UserDBO userExpected = UserDataProvider.getUserDBO();
        folderExpected.setPasswords(null);

        ArgumentCaptor<FolderDBO> folderDBOArgumentCaptor = ArgumentCaptor.forClass(FolderDBO.class);

        // When
        when(this.userRepository.findById(eq(userId))).thenReturn(Optional.of(userExpected));
        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(folderExpected);
        when(this.userRepository.save(any(UserDBO.class))).thenReturn(userExpected);
        FolderModel result = this.folderAdapter.createFolder(model,userId, null);

        // Then
        assertNotNull(result);

        verify(this.folderRepository).save(any(FolderDBO.class));
        verify(this.folderRepository).save(folderDBOArgumentCaptor.capture());
        FolderDBO folderSaved = folderDBOArgumentCaptor.getValue();

        assertEquals(folderId, folderSaved.getId_folder());
        assertEquals(0, result.getPasswordModels().size());
    }

    @Test
    void testUpdateFolder() {
        // Given
        UUID folderId = UUID.randomUUID();

        UserDBO userDBO = new UserDBO();
        userDBO.setEmail("test@example.com");
        userDBO.setUsername("username");

        UserModel userModel = new UserModel();
        userModel.setEmail(new EmailValueObject("test@example.com"));
        userModel.setUsername(new UsernameValueObject("username"));

        FolderModel model = new FolderModel("new_descriptionFolder", "new_icon", folderId, "new_folderName" , userModel,List.of(PasswordDataProvider.getPasswordModel()));

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", userDBO,List.of(PasswordDataProvider.getPasswordDBO()));

        FolderDBO dboExpected = new FolderDBO(folderId, "new_folderName", "new_icon", "new_descriptionFolder", userDBO, List.of(PasswordDataProvider.getPasswordDBO()));

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.of(dboFounded));
        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(dboExpected);
        FolderModel result = this.folderAdapter.updateFolder(model, folderId, List.of("PasswordName"));

        // Then
        assertNotNull(result);
        verify(this.folderRepository).findById(eq(folderId));
        verify(this.folderRepository).save(any(FolderDBO.class));

        assertEquals(model.getId_folder(), result.getId_folder());
        assertEquals(model.getName(), result.getName());
        assertEquals(model.getDescription(), result.getDescription());
        assertEquals("Name", result.getPasswordModels().getFirst().getName());
    }

    @Test
    void testUpdateFolderNotFound() {
        // Given
        UUID folderId = UUID.randomUUID();
        FolderModel model = new FolderModel("new_descriptionFolder", "new_icon", folderId, "new_folderName" , new UserModel(),List.of(PasswordDataProvider.getPasswordModel()));

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", new UserDBO(),List.of(PasswordDataProvider.getPasswordDBO()));

        FolderDBO dboExpected = new FolderDBO(folderId, "new_folderName", "new_icon", "new_descriptionFolder", new UserDBO(),List.of(PasswordDataProvider.getPasswordDBO()));

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.updateFolder(model, folderId, null);
        });

        assertEquals("Folder with id " + folderId + " not found", exception.getMessage());
    }

    @Test
    void testUpdateFolderWithoutPasswords() {
        // Given
        UserDBO userDBO = new UserDBO();
        userDBO.setEmail("test@example.com");
        userDBO.setUsername("username");

        UserModel userModel = new UserModel();
        userModel.setEmail(new EmailValueObject("test@example.com"));
        userModel.setUsername(new UsernameValueObject("username"));

        UUID folderId = UUID.randomUUID();
        FolderModel model = new FolderModel("new_descriptionFolder", "new_icon", folderId, "new_folderName" , userModel,List.of(PasswordDataProvider.getPasswordModel()));

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", userDBO,List.of(PasswordDataProvider.getPasswordDBO()));

        FolderDBO dboExpected = new FolderDBO(folderId, "new_folderName", "new_icon", "new_descriptionFolder", userDBO,List.of(PasswordDataProvider.getPasswordDBO()));

        dboExpected.setPasswords(new ArrayList<>());

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.of(dboFounded));
        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(dboExpected);
        FolderModel result = this.folderAdapter.updateFolder(model, folderId, null);

        // Then
        assertNotNull(result);
        verify(this.folderRepository).findById(eq(folderId));
        verify(this.folderRepository).save(any(FolderDBO.class));

        assertEquals(model.getId_folder(), result.getId_folder());
        assertEquals(model.getName(), result.getName());


        assertEquals(0, result.getPasswordModels().size());
    }

    @Test
    void testSetPasswordToFolder() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID passwordId = UUID.randomUUID();

        UserDBO userDBO = new UserDBO();
        userDBO.setEmail("test@example.com");
        userDBO.setUsername("username");

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", userDBO, new ArrayList<>());

        PasswordDBO passwordFounded = new PasswordDBO(passwordId, "passwordName", "url", LocalDateTime.now(), "test@test.com", "Password123@", "Description", null);

        FolderDBO dboExpected = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", userDBO,List.of(passwordFounded));

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.of(dboFounded));
        when(this.passwordRepository.findById(eq(passwordId))).thenReturn(Optional.of(passwordFounded));

        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(dboExpected);
        FolderModel result = this.folderAdapter.setPasswordToFolder(folderId, passwordId);

        // Then
        assertNotNull(result);

        verify(this.folderRepository).findById(eq(folderId));
        verify(this.passwordRepository).findById(eq(passwordId));

        assertEquals(result.getName(), dboFounded.getName());
        assertEquals(result.getDescription(), dboFounded.getDescription());
        assertEquals(result.getIcon(), dboFounded.getIcon());
        assertEquals(1, result.getPasswordModels().size());
        assertEquals(passwordFounded.getName(), result.getPasswordModels().getFirst().getName());
        assertEquals(passwordFounded.getDescription(), result.getPasswordModels().getFirst().getDescription());
        assertEquals(passwordFounded.getEmail_user(), result.getPasswordModels().getFirst().getEmail_user());
        assertEquals(passwordFounded.getPassword(), result.getPasswordModels().getFirst().getPassword());
    }
    @Test
    void testSetPasswordToFolderNotFound() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID passwordId = UUID.randomUUID();

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", new UserDBO(), new ArrayList<>());

        PasswordDBO passwordFounded = new PasswordDBO(passwordId, "passwordName", "url", LocalDateTime.now(), "test@test.com", "Password123@", "Description", null);

        FolderDBO dboExpected = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", new UserDBO(),List.of(passwordFounded));

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.setPasswordToFolder(folderId, passwordId);
        });

        assertEquals("Folder with id " + folderId + " not found", exception.getMessage());
    }

    @Test
    void testSetPasswordToFolderPasswordNotFound() {
        // Given
        UUID folderId = UUID.randomUUID();
        UUID passwordId = UUID.randomUUID();

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", new UserDBO(), new ArrayList<>());

        PasswordDBO passwordFounded = new PasswordDBO(passwordId, "passwordName", "url", LocalDateTime.now(), "test@test.com", "Password123@", "Description", null);

        FolderDBO dboExpected = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", new UserDBO(),List.of(passwordFounded));

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.of(dboFounded));
        when(this.passwordRepository.findById(eq(passwordId))).thenReturn(Optional.empty());

        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(dboExpected);

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.setPasswordToFolder(folderId, passwordId);
        });

        assertEquals("Password with id " + passwordId + " not found", exception.getMessage());
    }

    @Test
    void testDeleteFolder(){
        // Given
        UUID folderId = UUID.randomUUID();

        FolderDBO dboFounded = new FolderDBO(folderId, "folderName", "icon", "descriptionFolder", new UserDBO(), new ArrayList<>(List.of(PasswordDataProvider.getPasswordDBO())));

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.of(dboFounded));
        doNothing().when(this.folderRepository).delete(any(FolderDBO.class));
        String result = this.folderAdapter.deleteFolder(folderId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        verify(this.folderRepository).findById(eq(folderId));
        verify(this.folderRepository).delete(any(FolderDBO.class));

        assertEquals("Folder with id " + folderId + " deleted successfully", result);
    }

    @Test
    void testDeleteFolderNotFound(){
        // Given
        UUID folderId = UUID.randomUUID();

        // When
        when(this.folderRepository.findById(eq(folderId))).thenReturn(Optional.empty());

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.folderAdapter.deleteFolder(folderId);
        });

        assertEquals("Folder with id " + folderId + " not found", exception.getMessage());
    }
}
