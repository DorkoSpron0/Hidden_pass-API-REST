package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.Password;

import java.util.List;

public interface PasswordUseCases {
    List<Password> getAllPassword();
    Password getPasswordById(Password password);
    Password createPassword(Password password);
    Password editPassword(Password password);
    Password deletePassword(Password password);
}
