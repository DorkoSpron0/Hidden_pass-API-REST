package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;

import java.util.UUID;

public interface UserUseCases {
    UserDBO registerUser(UserDBO userModel);
    UserDBO getUserById(UUID id);
    UserDBO getUserByUsername(String username);
    String loginUser(UserDBO userModel);
    UserDBO updateUser(UUID id,UserDBO userModel);
    UserDBO deleteUser(UUID id);
}
