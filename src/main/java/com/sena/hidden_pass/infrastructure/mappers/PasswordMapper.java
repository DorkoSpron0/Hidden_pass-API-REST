package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.PasswordDTO;

public class PasswordMapper {
    public static PasswordDBO passwordModelToDBO(PasswordModel model){
        return new PasswordDBO(
                model.getId_password(),
                model.getName(),
                model.getUrl(),
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

    public static PasswordModel passwordDTOToModel(PasswordDTO dto){
        return new PasswordModel(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getEmail_user(),
                dto.getPassword(),
                dto.getUrl(),
                dto.getDateTime(),

                dto.getId_folder() != null ?
                        new FolderModel(
                                dto.getId_folder().getId_folder(),
                                dto.getId_folder().getName(),
                                dto.getId_folder().getDescription(),
                                dto.getId_folder().getIcon()
                        ) : null
        );
    }

    public boolean passTest(){
        return true;
    }
}
