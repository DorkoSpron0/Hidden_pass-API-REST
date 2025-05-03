package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;

import java.util.ArrayList;

public class FolderMapper {

    public static FolderDBO folderModelToDBO(FolderModel model){
        return new FolderDBO(
                model.getId_folder(),
                model.getName(),
                model.getIcon(),
                model.getDescription(),

                model.getUser() != null ? UserMapper.userModelToDBO(model.getUser()) : null,

                model.getPasswordModels() != null ? model.getPasswordModels().stream()
                        .map(PasswordMapper::passwordModelToDBO).toList() : new ArrayList<>()
        );
    }

    public static FolderModel folderDBOToModel(FolderDBO dbo){
        return new FolderModel(
                dbo.getDescription(),
                dbo.getIcon(),
                dbo.getId_folder(),
                dbo.getName(),
                dbo.getUser() != null ? UserMapper.userDBOToModel(dbo.getUser()) : null,
                dbo.getPasswords() != null ? dbo.getPasswords().stream()
                        .map(PasswordMapper::passwordDBOToModel).toList() : new ArrayList<>()
        );
    }

    public boolean passTest(){
        return true;
    }
}
