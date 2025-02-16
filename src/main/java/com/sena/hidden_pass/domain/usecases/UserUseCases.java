package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.UserModel;

public interface UserUseCases {
    UserModel registerUser(UserModel userModel);
    String loginUser(UserModel userModel);
    UserModel updateUser(UserModel userModel);
    UserModel deleteUser(UserModel userModel);
}
