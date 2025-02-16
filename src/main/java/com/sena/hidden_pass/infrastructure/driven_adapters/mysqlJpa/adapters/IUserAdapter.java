package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IUserAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtFilter jwtFilter;

    public IUserAdapter(IUserRepository iUserRepository, JwtFilter jwtFilter){
        this.userRepository = iUserRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public UserDBO registerUser(UserDBO userDBO) {
        return userRepository.save(userDBO);
    }

    @Override
    public UserDBO getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    public UserDBO getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not foun"));
    }

    @Override
    public String loginUser(UserDBO userModel) {
        UserDBO userFounded = getUserByUsername(userModel.getUsername());
        if(userModel.getMaster_password().equals(userFounded.getMaster_password())){
            return jwtFilter.generateToken(userFounded.getId_usuario());
        }

        throw new IllegalArgumentException("Password don't match");
    }

    @Override
    public UserDBO updateUser(UUID id, UserDBO userModel) {
        UserDBO userFounded = getUserById(id);
        userFounded.setEmail(userFounded.getEmail());
        userFounded.setUsername(userModel.getUsername());
        userFounded.setMaster_password(userModel.getMaster_password());
        return userRepository.save(userFounded);
    }

    @Override
    public UserDBO deleteUser(UUID id) {
        return null;
    }
}
