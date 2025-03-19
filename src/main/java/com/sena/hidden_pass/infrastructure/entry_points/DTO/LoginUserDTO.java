package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.MasterPasswordValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class LoginUserDTO {

    @Embedded
    private EmailValueObject email;

    @Embedded
    private MasterPasswordValueObject master_password;

    public UserDBO toDomain(){
        return new UserDBO(
                this.email,
                new HashSet<>(),
                this.master_password.getMaster_password(),
                new HashSet<>(),
                new HashSet<>(),
                null,
                null,
                null
        );
    }

    @Override
    public String toString() {
        return "LoginUserDTO{" +
                "email='" + email + '\'' +
                ", master_password='" + master_password + '\'' +
                '}';
    }
}
