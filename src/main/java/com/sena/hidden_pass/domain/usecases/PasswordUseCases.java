package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.PasswordModel;

import java.util.Set;
import java.util.UUID;

public interface PasswordUseCases {
    Set<PasswordModel> getAllPassword(UUID user_id);
    PasswordModel getPasswordById(UUID password_id);
    PasswordModel createPassword(PasswordModel password, UUID user_id, String folder_name);
    PasswordModel editPassword(PasswordModel password, UUID password_id, String folder_name);
    String deletePassword(UUID password_id);
}
