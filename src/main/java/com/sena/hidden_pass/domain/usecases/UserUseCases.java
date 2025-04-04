package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;

import java.util.UUID;

public interface UserUseCases {
    UserModel registerUser(UserModel UserModel);
    UserModel getUserById(UUID id);
    UserModel getUserByUsername(String username);
    UserModel getUserByUEmail(String email);
    String loginUser(UserModel UserModel);
    UserModel updateUser(UUID id,UserModel UserModel, String passwordSaved);
    UserModel deleteUser(UUID id);

    UserModel updateMasterPassword(String password, EmailValueObject email);
}
