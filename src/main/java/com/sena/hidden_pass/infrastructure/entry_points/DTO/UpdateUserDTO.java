package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.MasterPasswordValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserDTO {

    private UsernameValueObject username;
    private EmailValueObject email;
    private MasterPasswordValueObject master_password_saved;
    private MasterPasswordValueObject new_master_password;
    private String url_image;

    @Override
    public String toString() {
        return "UpdateUserDTO{" +
                "email=" + email +
                ", username=" + username +
                ", master_password_saved=" + master_password_saved +
                ", new_master_password=" + new_master_password +
                ", url_image='" + url_image + '\'' +
                '}';
    }
}
