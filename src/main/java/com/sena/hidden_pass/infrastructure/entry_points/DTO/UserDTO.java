package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.MasterPasswordValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Validated
public class UserDTO {

    private UUID id;

    @Valid
    private UsernameValueObject username;

    @Valid
    private EmailValueObject email;

    @Valid
    private MasterPasswordValueObject master_password;
    private Set<NoteDTO> noteList;

    private String url_image;
    private Set<PasswordDTO> passwordList;
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
