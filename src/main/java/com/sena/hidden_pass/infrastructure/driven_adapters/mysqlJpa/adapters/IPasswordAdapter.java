package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class IPasswordAdapter implements PasswordUseCases {

    private UserUseCases userAdapter;
    private IPasswordRepository passwordRepository;
    private IUserRepository userRepository;

    public IPasswordAdapter(IPasswordRepository passwordRepository, UserUseCases userAdapter, IUserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.userAdapter = userAdapter;
        this.userRepository = userRepository;
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
    public PasswordDBO createPassword(PasswordDBO password, UUID user_id) {
        PasswordDBO passwordSaved = passwordRepository.save(password);


        UserDBO userFounded = userAdapter.getUserById(user_id);
        userFounded.getPasswordList().add(password);
        userRepository.save(userFounded);

        return passwordSaved;
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
    public String deletePassword(UUID password_id) {
        PasswordDBO passwordFounded = getPasswordById(password_id);

        passwordRepository.delete(passwordFounded);

        return "Password with id " + password_id + " removed";
    }
}
