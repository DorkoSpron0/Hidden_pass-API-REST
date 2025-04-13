package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPasswordRepository;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import com.sena.hidden_pass.infrastructure.mappers.PasswordMapperFunction;
import com.sena.hidden_pass.infrastructure.mappers.UserMapperFunction;
import com.sena.hidden_pass.infrastructure.utils.AESUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class IPasswordAdapter implements PasswordUseCases {

    private UserUseCases userAdapter;
    private IPasswordRepository passwordRepository;
    private IUserRepository userRepository;

    private final Function<UUID, UserModel> functionFindUserById = (id) -> userAdapter.getUserById(id);

    private final Function<UUID, PasswordDBO> functionFindPasswordDBOById = (id) -> passwordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Password not found"));

    private final Function<PasswordDBO, PasswordDBO> functionRepositorySavePassword = (dbo) -> passwordRepository.save(dbo);

    private final Function<UserDBO, UserDBO> functionRepositorySaveUser = (dbo) -> userRepository.save(dbo);

    private final BiFunction<PasswordDBO, UUID, String> functionRepositoryDeleteUser = (dbo, id) -> {
        passwordRepository.delete(dbo);
        return "Password with id " + id + " removed";
    };

    private final Function<PasswordModel, PasswordModel> functionDecryptPassword = (model) -> {
        try{
            return new PasswordModel(
                    model.getId_password(),
                    model.getName(),
                    model.getDescription(),
                    model.getEmail_user(),
                    AESUtil.decrypt(model.getPassword()),
                    model.getUrl(),
                    model.getDateTime(),
                    model.getId_folder()
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    };

    private final Function<PasswordModel, PasswordModel> functionEncryptPassword = (model) -> {
        try{
            return new PasswordModel(
                    model.getId_password(),
                    model.getName(),
                    model.getDescription(),
                    model.getEmail_user(),
                    AESUtil.encrypt(model.getPassword()),
                    model.getUrl(),
                    model.getDateTime(),
                    model.getId_folder()
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    };

    private final BiFunction<PasswordModel, UUID, PasswordModel> functionSavePassword = (PasswordModel model, UUID id) -> {
        PasswordModel password = functionEncryptPassword.apply(model);
        PasswordDBO passwordSaved = functionRepositorySavePassword.apply(PasswordMapperFunction.functionModelToDBO.apply(password));

        UserDBO userFounded = UserMapperFunction.functionModelToDBO.apply(functionFindUserById.apply(id));

        // NOTE - JUNTA EL SET DE PASSWORDS Y AÑADE EL NUEVO EN UN MISMO SET
        Set<PasswordDBO> updatedPasswords = Stream.concat(
                userFounded.getPasswordList().stream(),
                Stream.of(passwordSaved)
        ).collect(Collectors.toSet());

        UserDBO updateUser = new UserDBO(
                userFounded.getId_usuario(),
                userFounded.getUsername(),
                userFounded.getEmail(),
                userFounded.getMaster_password(),
                userFounded.getUrl_image(),
                Optional.ofNullable(userFounded.getNoteList()).orElse(new HashSet<>()),
                updatedPasswords,
                Optional.ofNullable(userFounded.getFolderList()).orElse(new HashSet<>()),
                userFounded.getSecurityCodes()
        );

        functionRepositorySaveUser.apply(updateUser);

        return PasswordMapperFunction.functionDBOTOModel.apply(passwordSaved);
    };

    private final BiFunction<PasswordDBO, PasswordModel, PasswordDBO> functionUpdatePassword = (PasswordDBO dbo, PasswordModel model) -> {
        dbo.setName(model.getName());
        try{
            return new PasswordDBO(
                    dbo.getId_password(),
                    model.getName(),
                    model.getUrl(),
                    LocalDateTime.now(),
                    model.getEmail_user(),
                    AESUtil.encrypt(model.getPassword()),
                    model.getDescription(),
                    model.getId_folder() != null ? new FolderDBO(
                            model.getId_folder().getId_folder(),
                            model.getId_folder().getName(),
                            model.getId_folder().getIcon(),
                            model.getId_folder().getDescription()) : null
            );
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    };

    @Override
    public Set<PasswordModel> getAllPassword(UUID user_id) {
        UserDBO userFounded = UserMapperFunction.functionModelToDBO.apply(functionFindUserById.apply(user_id));
        return userFounded.getPasswordList().stream().map(
                passwordDBO -> {
                    try {
                        passwordDBO.setPassword(AESUtil.decrypt(passwordDBO.getPassword()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return PasswordMapperFunction.functionDBOTOModel.apply(passwordDBO);
                }
        ).collect(Collectors.toSet());
    }

    @Override
    public PasswordModel getPasswordById(UUID password_id) {
        try{
            PasswordModel passwordFounded = PasswordMapperFunction.functionDBOTOModel.apply(functionFindPasswordDBOById.apply(password_id));
            return functionDecryptPassword.apply(passwordFounded);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PasswordModel createPassword(PasswordModel password, UUID user_id) {
        try{
            return functionSavePassword.apply(password, user_id);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PasswordModel editPassword(PasswordModel password, UUID password_id) {
        PasswordDBO passwordFounded = functionFindPasswordDBOById.apply(password_id);

        PasswordDBO dbo = functionUpdatePassword.apply(passwordFounded, password);

        return PasswordMapperFunction.functionDBOTOModel.apply(functionRepositorySavePassword.apply(dbo));
    }

    @Override
    public String deletePassword(UUID password_id) {
        PasswordDBO passwordFounded = functionFindPasswordDBOById.apply(password_id);
        return functionRepositoryDeleteUser.apply(passwordFounded, password_id);
    }
}
