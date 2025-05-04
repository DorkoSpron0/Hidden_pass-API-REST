package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.FolderModel;

import java.util.List;
import java.util.UUID;

public interface FolderUseCases {
    List<FolderModel> getAllFolders(UUID userId);
    FolderModel getFolderById(UUID id);
    FolderModel getFolderByName(String name);
    FolderModel createFolder(FolderModel folder, UUID userId, List<String> passwordsNames);
    FolderModel updateFolder(FolderModel folder, UUID id, List<String> passwordsNames);
    FolderModel setPasswordToFolder(UUID folderId, UUID passwordId);
    String deleteFolder(UUID id);
}
