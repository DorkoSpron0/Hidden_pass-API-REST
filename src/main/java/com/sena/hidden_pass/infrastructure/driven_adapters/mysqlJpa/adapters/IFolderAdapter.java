package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.usecases.FolderUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IFolderRepository;
import com.sena.hidden_pass.infrastructure.mappers.FolderMapper;

import java.util.List;
import java.util.UUID;

public class IFolderAdapter implements FolderUseCases {

    private IFolderRepository folderRepository;

    public IFolderAdapter(IFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public List<FolderModel> getAllFolders() {
        return folderRepository.findAll().stream().map(FolderMapper::folderDBOToModel).toList();
    }

    @Override
    public FolderModel getFolderById(UUID id) {
        FolderDBO folderFounded = folderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Folder with id " + id + " not found"));
        return FolderMapper.folderDBOToModel(folderFounded);
    }

    @Override
    public FolderModel createFolder(FolderModel folder) {
        FolderDBO folderSaved = folderRepository.save(FolderMapper.folderModelToDBO(folder));
        return FolderMapper.folderDBOToModel(folderSaved);
    }

    @Override
    public FolderModel updateFolder(FolderModel folder) {
        return null;
    }

    @Override
    public FolderModel setPasswordToFolder(PasswordModel password) {
        return null;
    }

    @Override
    public String deleteFolder(FolderModel folder) {
        return "";
    }
}
