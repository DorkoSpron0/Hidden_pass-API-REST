package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PasswordUseCases {
    Set<PasswordDBO> getAllPassword(UUID user_id);
    PasswordDBO getPasswordById(UUID password_id);
    PasswordDBO createPassword(PasswordDBO password, UUID user_id);
    PasswordDBO editPassword(PasswordDBO password, UUID password_id);
    String deletePassword(UUID password_id);
}
