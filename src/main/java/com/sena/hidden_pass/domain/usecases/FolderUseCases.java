package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.Folder;

import java.util.List;

public interface FolderUseCases {
    List<Folder> getAllFolders();
    Folder getFolderById(Folder folder);
    Folder createFolder(Folder folder);
    Folder updateFolder(Folder folder);
    Folder deleteFolder(Folder folder);
}
