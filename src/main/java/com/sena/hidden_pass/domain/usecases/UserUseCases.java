package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;

import java.util.UUID;

public interface UserUseCases {
    UserDBO registerUser(UserDBO userModel);
    UserDBO getUserById(UUID id);
    UserDBO getUserByUsername(String username);
    UserDBO getUserByUEmail(String email);
    String loginUser(UserDBO userModel);
    UserDBO updateUser(UUID id,UserDBO userModel);
    UserDBO deleteUser(UUID id);

    UserDBO updateMasterPassword(String password, EmailValueObject email);
}
