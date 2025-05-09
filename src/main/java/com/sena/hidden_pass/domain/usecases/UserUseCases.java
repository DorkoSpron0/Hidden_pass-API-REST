package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.UserLoginModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;

import java.util.UUID;

public interface UserUseCases {
    UserModel registerUser(UserModel UserModel);
    UserModel getUserById(UUID id);
    UserModel getUserByUsername(String username);
    UserModel getUserByUEmail(String email);
    UserLoginModel loginUser(UserModel UserModel);
    UserModel updateUser(UUID id,UserModel UserModel);
    String deleteUser(UUID id, String current_password);

    UserModel recoverMasterPassword(String password, EmailValueObject email);

    UserModel updateMasterPassword(UUID id, String current_password, String new_password);
}
