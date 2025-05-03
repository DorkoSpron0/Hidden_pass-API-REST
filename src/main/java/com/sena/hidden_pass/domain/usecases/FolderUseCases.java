package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.FolderModel;

import java.util.List;
import java.util.UUID;

public interface FolderUseCases {
    List<FolderModel> getAllFolders(UUID userId);
    FolderModel getFolderById(UUID id);
    FolderModel createFolder(FolderModel folder, UUID userId);
    FolderModel updateFolder(FolderModel folder, UUID id);
    FolderModel setPasswordToFolder(UUID folderId, UUID passwordId);
    String deleteFolder(UUID id);
}
