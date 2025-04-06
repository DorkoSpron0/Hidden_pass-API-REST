package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.*;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UpdateUserDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;

import java.util.HashSet;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDBO userModelToDBO(UserModel model){
        return new UserDBO(
                model.getId_usuario(),
                model.getUsername(),
                model.getEmail(),
                model.getMaster_password(),
                model.getUrl_image(),


                model.getNoteList() != null ? model.getNoteList().stream()
                        .map(noteModel -> new NoteDBO(
                                noteModel.getId_note(),
                                noteModel.getId_priority() != null ?
                                        new PriorityDBO(
                                                noteModel.getId_priority().getName()
                                        )
                                        :null,
                                noteModel.getTitle(),
                                noteModel.getDescription()
                        )).collect(Collectors.toSet()) : new HashSet<>(),



                model.getPasswordList() != null ? model.getPasswordList().stream()
                        .map(passwordModel -> new PasswordDBO(
                                passwordModel.getId_password(),
                                passwordModel.getName(),
                                passwordModel.getUrl(),
                                passwordModel.getDateTime(),
                                passwordModel.getEmail_user(),
                                passwordModel.getPassword(),
                                passwordModel.getDescription(),
                                passwordModel.getId_folder() != null ?
                                        new FolderDBO(
                                                passwordModel.getId_folder().getId_folder(),
                                                passwordModel.getId_folder().getName(),
                                                passwordModel.getId_folder().getIcon(),
                                                passwordModel.getId_folder().getDescription()
                                        )
                                        : null
                        )).collect(Collectors.toSet()) : new HashSet<>(),



                model.getFolderList() != null ? model.getFolderList().stream()
                        .map(folderModel -> new FolderDBO(
                                folderModel.getId_folder(),
                                folderModel.getName(),
                                folderModel.getIcon(),
                                folderModel.getDescription())
                        ).collect(Collectors.toSet()) : new HashSet<>(),


                model.getSecurityCodes() != null ?
                        new SecurityCodesDBO(model.getSecurityCodes().getSecurity_code()) : null
        );
    }

    public static UserModel userDBOToModel(UserDBO dbo){
        return new UserModel(
                dbo.getId_usuario(),
                dbo.getEmail(),
                dbo.getUsername(),
                dbo.getMaster_password(),
                dbo.getUrl_image(),

                dbo.getPasswordList() != null ? dbo.getPasswordList().stream()
                        .map(passwordDBO -> new PasswordModel(
                                passwordDBO.getId_password(),
                                passwordDBO.getName(),
                                passwordDBO.getDescription(),
                                passwordDBO.getEmail_user(),
                                passwordDBO.getPassword(),
                                passwordDBO.getUrl(),
                                passwordDBO.getDateTime(),

                                passwordDBO.getId_folder() != null ?
                                        new FolderModel(
                                                passwordDBO.getId_folder().getId_folder(),
                                                passwordDBO.getId_folder().getName(),
                                                passwordDBO.getId_folder().getDescription(),
                                                passwordDBO.getId_folder().getIcon()
                                        ) : null
                        )).collect(Collectors.toSet()) : new HashSet<>(),

                dbo.getNoteList() != null ? dbo.getNoteList().stream()
                        .map(noteDBO -> new NoteModel(
                                noteDBO.getId_note(),
                                noteDBO.getTitle(),
                                noteDBO.getDescription(),
                                noteDBO.getId_priority() != null ?
                                        new PriorityModel(
                                                noteDBO.getId_priority().getId_priority(),
                                                noteDBO.getId_priority().getName()
                                        ) : null
                        )).collect(Collectors.toSet()) :  new HashSet<>(),

                dbo.getFolderList() != null ? dbo.getFolderList().stream()
                        .map(folderDBO -> new FolderModel(
                                folderDBO.getId_folder(),
                                folderDBO.getName(),
                                folderDBO.getDescription(),
                                folderDBO.getIcon()
                        )).collect(Collectors.toSet()) : new HashSet<>(),


                dbo.getSecurityCodes() != null ?
                        new SecurityCodesModel(dbo.getSecurityCodes().getSecurity_code()) : null
        );
    }

    public static UserModel userDTOToModel(UserDTO dto){
        return new UserModel(
                dto.getId(),
                dto.getEmail(),
                dto.getUsername(),
                dto.getMaster_password().getMaster_password(),
                dto.getUrl_image(),
                null,
                null,
                null,
                null
        );
    }

    public static UserModel updateUserDTOToModel(UpdateUserDTO dto){
        return new UserModel(
                null,
                dto.getEmail(),
                dto.getUsername(),
                null,
                dto.getUrl_image(),
                null,
                null,
                null,
                null
        );
    }
}
