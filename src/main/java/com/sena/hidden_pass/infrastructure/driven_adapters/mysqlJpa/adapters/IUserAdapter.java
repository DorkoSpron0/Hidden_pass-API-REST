package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IUserAdapter implements UserUseCases {

    private final IUserRepository userRepository;
    private final JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;

    public IUserAdapter(IUserRepository iUserRepository, JwtFilter jwtFilter, PasswordEncoder passwordEncoder){
        this.userRepository = iUserRepository;
        this.jwtFilter = jwtFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserModel registerUser(UserModel UserDBO) {

        UserDBO.setMaster_password(passwordEncoder.encode(UserDBO.getMaster_password()));

        return UserMapper.userDBOToModel(userRepository.save(UserMapper.userModelToDBO(UserDBO)));
    }

    @Override
    public UserModel getUserById(UUID id) {
        return UserMapper.userDBOToModel(userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found")));
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return UserMapper.userDBOToModel(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found")));
    }

    @Override
    public UserModel getUserByUEmail(String email) {
        return UserMapper.userDBOToModel(userRepository.findByEmail(new EmailValueObject(email)).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found")));
    }

    @Override
    public String loginUser(UserModel UserDBO) {
        UserDBO userFounded = userRepository.findByEmail_Email(UserDBO.getEmail().getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with email " + UserDBO.getEmail() + " not found"));

        if(!matchPassword(UserDBO.getMaster_password(), userFounded.getMaster_password())) throw new IllegalArgumentException("Password don't match");

        return jwtFilter.generateToken(userFounded.getId_usuario());
    }

    @Override
    public UserModel updateUser(UUID id, UserModel UserDBO) {
        UserDBO userFounded = UserMapper.userModelToDBO(getUserById(id));
        userFounded.setEmail(userFounded.getEmail());
        userFounded.setUsername(UserDBO.getUsername());
        userFounded.setMaster_password(UserDBO.getMaster_password());

        userFounded.setPasswordList(userFounded.getPasswordList());
        userFounded.setNoteList(userFounded.getNoteList());
        userFounded.setFolderList(userFounded.getFolderList());

        return UserMapper.userDBOToModel(userRepository.save(userFounded));
    }

    @Override
    public UserModel deleteUser(UUID id) {
        return null;
    }

    @Override
    public UserModel updateMasterPassword(String password, UUID user_id) {
        UserDBO userFounded = UserMapper.userModelToDBO(getUserById(user_id));

        userFounded.setMaster_password(passwordEncoder.encode(password));

        userRepository.save(userFounded);

        return UserMapper.userDBOToModel(userFounded);
    }


    private boolean matchPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
