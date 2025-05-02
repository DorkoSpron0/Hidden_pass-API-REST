package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.PasswordDataProvider;
import com.sena.hidden_pass.UserDataProvider;
import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IFolderRepository;
import com.sena.hidden_pass.infrastructure.mappers.FolderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UT_FolderAdapterTest {

    @Mock
    private IFolderRepository folderRepository;

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
        FolderDBO dbo = new FolderDBO(UUID.randomUUID(), "folderName", "icon", "descriptionFolder", new UserDBO(),List.of(PasswordDataProvider.getPasswordDBO()));

        // When
        when(this.folderRepository.findAll()).thenReturn(List.of(dbo));
        List<FolderModel> folderModels = this.folderAdapter.getAllFolders();

        System.out.println(folderModels);

        // Then
        assertNotNull(folderModels);
        assertEquals("folderName", folderModels.getFirst().getName());
        assertEquals("descriptionFolder", folderModels.getFirst().getDescription());
        assertEquals("icon", folderModels.getFirst().getIcon());
        assertEquals("Name", folderModels.getFirst().getPasswordModels().getFirst().getName());
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
    void testCreateFolder() {
        // Given
        UUID folderId = UUID.randomUUID();
        FolderModel model = new FolderModel("descriptionFolder", "icon", folderId, "folderName" , new UserModel(),List.of(PasswordDataProvider.getPasswordModel()));

        FolderDBO folderExpected = new FolderDBO(UUID.randomUUID(), "folderName", "icon", "descriptionFolder", new UserDBO(),List.of(PasswordDataProvider.getPasswordDBO()));

        ArgumentCaptor<FolderDBO> folderDBOArgumentCaptor = ArgumentCaptor.forClass(FolderDBO.class);

        // When
        when(this.folderRepository.save(any(FolderDBO.class))).thenReturn(folderExpected);
        FolderModel result = this.folderAdapter.createFolder(model);

        // Then
        assertNotNull(result);

        verify(this.folderRepository).save(any(FolderDBO.class));
        verify(this.folderRepository).save(folderDBOArgumentCaptor.capture());
        FolderDBO folderSaved = folderDBOArgumentCaptor.getValue();

        assertEquals(folderId, folderSaved.getId_folder());
    }

}
