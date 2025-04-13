package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.application.config.JwtFilter;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.UserMapperFunction;
import com.sena.hidden_pass.infrastructure.services.MailService;
import com.sena.hidden_pass.infrastructure.services.MailTemplate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class IUserAdapter implements UserUseCases {

    private IUserRepository userRepository;
    private JwtFilter jwtFilter;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    private final Function<UUID, UserDBO> functionFindDBOById =
            (UUID id) -> userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

    private final Function<String, UserDBO> functionFindDBOByEmail =
            (String email) -> userRepository.findByEmail_Email(email).orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));

    private final Function<String, UserDBO> functionFindDBOByName =
            (String name) -> userRepository.findByUsername(name).orElseThrow(() -> new UsernameNotFoundException("User with username " + name + " not found"));

    private final Function<UserModel, UserModel> hashPassword = (UserModel model) -> {
        UserModel copy = new UserModel(model);
        copy.setMaster_password(passwordEncoder.encode(model.getMaster_password()));
        return copy;
    };

    private final Function<UserModel, UserModel> registerUser =
            hashPassword
                    .andThen(UserMapperFunction.functionModelToDBO)
                    .andThen(userDBO -> userRepository.save(userDBO))
                    .andThen(UserMapperFunction.functionDBOToModel);

    private final BiFunction<UserDBO, UserModel, UserModel> functionUpdateUser = (UserDBO userFounded, UserModel userModel) -> {
        userFounded.setEmail(userModel.getEmail());
        userFounded.setUsername(userModel.getUsername());
        userFounded.setMaster_password(userFounded.getMaster_password());

        userFounded.setUrl_image(userModel.getUrl_image());

        userFounded.setPasswordList(userFounded.getPasswordList());
        userFounded.setNoteList(userFounded.getNoteList());
        userFounded.setFolderList(userFounded.getFolderList());

        return UserMapperFunction.functionDBOToModel.apply(userFounded);
    };

    private final BiFunction<UserDBO, String, UserModel> functionChangeMasterPassword = (UserDBO dbo, String newPassword) -> {
        dbo.setMaster_password(passwordEncoder.encode(newPassword));
        userRepository.save(dbo);
        return UserMapperFunction.functionDBOToModel.apply(dbo);
    };

    private final BiFunction<String, String , Boolean> functionMatchPassword = (String raw, String saved ) -> passwordEncoder.matches(raw, saved);

    @Transactional
    @Override
    public UserModel registerUser(UserModel user) {
        try{
            String username = user.getUsername().getUsername(); // Asumiendo que getUsername() devuelve el nombre directamente

            String body = MailTemplate.buildTemplate(username);
            //mailService.sendEmailAyncImpl(user.getEmail().getEmail(), "BIENVENIDO A HIDDEN PASS", body);
            return registerUser.apply(user);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public UserModel getUserById(UUID id) {
        return UserMapperFunction.functionDBOToModel.apply(functionFindDBOById.apply(id));
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return UserMapperFunction.functionDBOToModel.apply(functionFindDBOByName.apply(username));
    }

    @Override
    public UserModel getUserByUEmail(String email) {
        return UserMapperFunction.functionDBOToModel.apply(functionFindDBOByEmail.apply(email));
    }

    @Override
    public String loginUser(UserModel user) {
        UserDBO userFounded = functionFindDBOByEmail.apply(user.getEmail().getEmail());

        if(!functionMatchPassword.apply(user.getMaster_password(), userFounded.getMaster_password())) throw new IllegalArgumentException("Password don't match");

        return jwtFilter.generateToken(userFounded.getId_usuario());
    }

    @Transactional
    @Override
    public UserModel updateUser(UUID id, UserModel userModel) {
        UserDBO userFounded = functionFindDBOById.apply(id);

        return functionUpdateUser.apply(userFounded, userModel);
    }

    @Override
    public String deleteUser(UUID id) {
        UserDBO dbo = functionFindDBOById.apply(id);
        userRepository.delete(dbo);

        return "User with id " + id + " deleted successfully";
    }

    @Transactional
    @Override
    public UserModel recoverMasterPassword(String password, EmailValueObject email) {
        UserDBO userFounded = functionFindDBOByEmail.apply(email.getEmail());
        return functionChangeMasterPassword.apply(userFounded, password);
    }

    @Transactional
    @Override
    public UserModel updateMasterPassword(UUID id, String current_password, String new_password) {
        UserDBO userFounded = functionFindDBOById.apply(id);
        if(!functionMatchPassword.apply(current_password, userFounded.getMaster_password())) throw new IllegalArgumentException("La contraseña ingresada no es correcta");
        return functionChangeMasterPassword.apply(userFounded, new_password);
    }
}
