package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String email;
    private String master_password;
    private Set<NoteDBO> noteList;
    private Set<PasswordDBO> passwordList;
    private Set<FolderDBO> folderList;

    public UserDBO toDomain(){
        return new UserDBO(this.email, this.folderList, this.master_password, this.noteList, this.passwordList, this.username);
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
