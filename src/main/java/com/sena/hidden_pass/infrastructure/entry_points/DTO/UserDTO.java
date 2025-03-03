package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    @Pattern(
            regexp = "^[A-Za-z0-9\\d]{4,}$",
            message = "Debe tener al menos 4 caracteres y solo puede contener letras y números."
    )
    private String username;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El correo electrónico no es válido. Debe seguir el formato: ejemplo@dominio.com"
    )
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, un número y un símbolo especial."
    )
    private String master_password;
    private Set<NoteDBO> noteList;
    private String url_image;
    private Set<PasswordDBO> passwordList;
    private Set<FolderDBO> folderList;
    private Set<SecurityCodesDBO> securityCodes;

    public UserDBO toDomain(){
        return new UserDBO(this.email, this.folderList, this.master_password, this.noteList, this.passwordList, this.username, this.securityCodes, this.url_image);
    }

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
