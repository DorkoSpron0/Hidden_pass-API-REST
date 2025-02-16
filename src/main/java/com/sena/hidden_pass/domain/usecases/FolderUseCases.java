package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.Folder;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;

import java.util.List;

public interface FolderUseCases {
    List<FolderDBO> getAllFolders();
    FolderDBO getFolderById(Folder folder);
    FolderDBO createFolder(Folder folder);
    FolderDBO updateFolder(Folder folder);
    FolderDBO deleteFolder(Folder folder);
}
