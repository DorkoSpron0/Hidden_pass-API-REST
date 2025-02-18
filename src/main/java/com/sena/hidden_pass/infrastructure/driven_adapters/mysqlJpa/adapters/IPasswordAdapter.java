package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class IPasswordAdapter implements PasswordUseCases {

    private UserUseCases userAdapter;
    private IPasswordRepository passwordRepository;

    public IPasswordAdapter(UserUseCases iUserAdapter, IPasswordRepository iPasswordRepository){
        this.userAdapter = iUserAdapter;
        this.passwordRepository = iPasswordRepository;
    }

    @Override
    public Set<PasswordDBO> getAllPassword(UUID user_id) {
        UserDBO userFounded = userAdapter.getUserById(user_id);
        return userFounded.getPasswordList();
    }

    @Override
    public PasswordDBO getPasswordById(UUID password_id) {
        return passwordRepository.findById(password_id).orElseThrow(() -> new IllegalArgumentException("Password not found"));
    }

    @Override
    public PasswordDBO createPassword(PasswordDBO password) {
        return passwordRepository.save(password);
    }

    @Override
    public PasswordDBO editPassword(PasswordDBO password, UUID password_id) {
        PasswordDBO passwordFounded = getPasswordById(password_id);

        passwordFounded.setName(password.getName());
        passwordFounded.setPassword(password.getPassword());
        passwordFounded.setEmail_user(password.getEmail_user());
        passwordFounded.setUrl(password.getUrl());
        passwordFounded.setId_folder(password.getId_folder());
        passwordFounded.setDescription(password.getDescription());
        passwordFounded.setDateTime(LocalDateTime.now());

        return passwordRepository.save(passwordFounded);
    }

    @Override
    public String deletePassword(PasswordDBO password, UUID password_id) {
        PasswordDBO passwordFounded = getPasswordById(password_id);

        passwordRepository.delete(passwordFounded);

        return "Password with id " + password_id + " removed";
    }
}
