package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.FolderModel;

import java.util.List;

public interface FolderUseCases {
    List<FolderModel> getAllFolders();
    FolderModel getFolderById(FolderModel folder);
    FolderModel createFolder(FolderModel folder);
    FolderModel updateFolder(FolderModel folder);
    FolderModel deleteFolder(FolderModel folder);
}
