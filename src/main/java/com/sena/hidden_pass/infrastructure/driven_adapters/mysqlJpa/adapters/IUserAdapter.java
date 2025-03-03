package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
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
    public UserDBO registerUser(UserDBO userDBO) {

        System.out.println(userDBO.toString());
        userDBO.setMaster_password(passwordEncoder.encode(userDBO.getMaster_password()));
        System.out.println("--------------------------------" + userDBO.getMaster_password());

        System.out.println(userDBO.getMaster_password());
        return userRepository.save(userDBO);
    }

    @Override
    public UserDBO getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    public UserDBO getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public UserDBO getUserByUEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public String loginUser(UserDBO userModel) {
        UserDBO userFounded = userRepository.findByEmail_Email(userModel.getEmail().getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with email " + userModel.getEmail() + " not found"));

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA " + userModel.getMaster_password());
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBB" + userFounded.getMaster_password());

        if(!matchPassword(userModel.getMaster_password(), userFounded.getMaster_password())) throw new IllegalArgumentException("Password don't match");

        return jwtFilter.generateToken(userFounded.getId_usuario());
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


    private boolean matchPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private boolean matchEmail(String emailRequest, String emailSaved){
        return emailRequest.equals(emailSaved);
    }
}
