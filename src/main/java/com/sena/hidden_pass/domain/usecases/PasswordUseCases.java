package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.Password;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;

import java.util.List;

public interface PasswordUseCases {
    List<PasswordDBO> getAllPassword();
    PasswordDBO getPasswordById(Password password);
    PasswordDBO createPassword(Password password);
    PasswordDBO editPassword(Password password);
    PasswordDBO deletePassword(Password password);
}
