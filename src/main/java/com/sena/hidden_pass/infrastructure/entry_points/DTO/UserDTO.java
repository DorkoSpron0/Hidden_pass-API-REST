package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.MasterPasswordValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private UsernameValueObject username;
    private EmailValueObject email;
    private MasterPasswordValueObject master_password;
    private Set<NoteDBO> noteList;
    private String url_image;
    private Set<PasswordDBO> passwordList;
    private Set<FolderDBO> folderList;
    private SecurityCodesDBO securityCodes;

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", folderList=" + folderList +
                ", master_password='" + master_password + '\'' +
                ", noteList=" + noteList +
                ", passwordList=" + passwordList +
                ", username='" + username + '\'' +
                '}';
    }
}
