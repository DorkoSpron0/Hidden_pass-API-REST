package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;

import java.util.List;
import java.util.UUID;

public interface FolderUseCases {
    List<FolderModel> getAllFolders();
    FolderModel getFolderById(UUID id);
    FolderModel createFolder(FolderModel folder);
    FolderModel updateFolder(FolderModel folder);
    FolderModel setPasswordToFolder(PasswordModel password);
    String deleteFolder(FolderModel folder);
}
