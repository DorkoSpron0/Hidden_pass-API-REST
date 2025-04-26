package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.PasswordMapper;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import com.sena.hidden_pass.infrastructure.utils.AESUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IPasswordAdapter implements PasswordUseCases {

    private UserUseCases userAdapter;
    private IPasswordRepository passwordRepository;
    private IUserRepository userRepository;
    private AESUtil aesUtil;

    public IPasswordAdapter(IPasswordRepository passwordRepository, UserUseCases userAdapter, IUserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.userAdapter = userAdapter;
        this.userRepository = userRepository;
    }

    @Override
    public Set<PasswordModel> getAllPassword(UUID user_id) {
        UserDBO userFounded = UserMapper.userModelToDBO(userAdapter.getUserById(user_id));
        return userFounded.getPasswordList().stream().map(
                passwordDBO -> {
                    try {
                        passwordDBO.setPassword(aesUtil.decrypt(passwordDBO.getPassword()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return PasswordMapper.passwordDBOToModel(passwordDBO);
                }
        ).collect(Collectors.toSet());
    }

    @Override
    public PasswordModel getPasswordById(UUID password_id) {
        try{
            PasswordModel passwordFounded = PasswordMapper.passwordDBOToModel(passwordRepository.findById(password_id).orElseThrow(() -> new IllegalArgumentException("Password not found")));

            passwordFounded.setPassword(aesUtil.decrypt(passwordFounded.getPassword()));
            return passwordFounded;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PasswordModel createPassword(PasswordModel password, UUID user_id) {
        try{
            // Guardar la contraseña primero
            password.setPassword(aesUtil.encrypt(password.getPassword()));
            PasswordDBO passwordSaved = passwordRepository.save(PasswordMapper.passwordModelToDBO(password));

            // Obtener el usuario
            UserDBO userFounded = UserMapper.userModelToDBO(userAdapter.getUserById(user_id));

            // Añadir la contraseña guardada (persistida) a la lista
            userFounded.getPasswordList().add(passwordSaved);  // Usar 'passwordSaved' en lugar de un nuevo objeto

            // Guardar el usuario actualizado
            userRepository.save(userFounded);

            return PasswordMapper.passwordDBOToModel(passwordSaved);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PasswordModel editPassword(PasswordModel password, UUID password_id) {
        PasswordDBO passwordFounded = PasswordMapper.passwordModelToDBO(getPasswordById(password_id));

        passwordFounded.setName(password.getName());
        try{
            passwordFounded.setPassword(aesUtil.encrypt(password.getPassword()));
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        passwordFounded.setEmail_user(password.getEmail_user());
        passwordFounded.setUrl(password.getUrl());
        if(password.getId_folder() != null){
            passwordFounded.setId_folder(new FolderDBO(
                    password.getId_folder().getId_folder(),
                    password.getId_folder().getName(),
                    password.getId_folder().getIcon(),
                    password.getId_folder().getDescription())
            );
        }
        passwordFounded.setDescription(password.getDescription());
        passwordFounded.setDateTime(LocalDateTime.now());

        return PasswordMapper.passwordDBOToModel(passwordRepository.save(passwordFounded));
    }

    @Override
    public String deletePassword(UUID password_id) {
        PasswordDBO passwordFounded = PasswordMapper.passwordModelToDBO(getPasswordById(password_id));

        passwordRepository.delete(passwordFounded);

        return "Password with id " + password_id + " removed";
    }
}
