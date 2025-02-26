package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;

import java.util.List;

public interface FolderUseCases {
    List<FolderDBO> getAllFolders();
    FolderDBO getFolderById(FolderDBO folder);
    FolderDBO createFolder(FolderDBO folder);
    FolderDBO updateFolder(FolderDBO folder);
    FolderDBO deleteFolder(FolderDBO folder);
}
