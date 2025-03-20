package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;

public class PasswordMapper {
    public static PasswordDBO passwordModelToDBO(PasswordModel model){
        return new PasswordDBO(
                model.getId_password(),
                model.getName(),
                model.getDescription(),
                model.getDateTime(),
                model.getEmail_user(),
                model.getPassword(),
                model.getDescription(),

                model.getId_folder() != null ?
                        new FolderDBO(
                                model.getId_folder().getId_folder(),
                                model.getId_folder().getName(),
                                model.getId_folder().getIcon(),
                                model.getId_folder().getDescription()
                        ) : null
        );
    }

    public static PasswordModel passwordDBOToModel(PasswordDBO dbo){
        return new PasswordModel(
                dbo.getId_password(),
                dbo.getName(),
                dbo.getDescription(),
                dbo.getEmail_user(),
                dbo.getPassword(),
                dbo.getUrl(),
                dbo.getDateTime(),
                dbo.getId_folder() != null ?
                        new FolderModel(
                                dbo.getId_folder().getId_folder(),
                                dbo.getId_folder().getName(),
                                dbo.getId_folder().getDescription(),
                                dbo.getId_folder().getIcon()
                        ) : null
        );
    }
}
