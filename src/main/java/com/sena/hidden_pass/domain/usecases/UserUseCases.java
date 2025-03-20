package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.UserModel;

import java.util.UUID;

public interface UserUseCases {
    UserModel registerUser(UserModel UserModel);
    UserModel getUserById(UUID id);
    UserModel getUserByUsername(String username);
    UserModel getUserByUEmail(String email);
    String loginUser(UserModel UserModel);
    UserModel updateUser(UUID id,UserModel UserModel);
    UserModel deleteUser(UUID id);

    UserModel updateMasterPassword(String password, UUID user_id);
}
