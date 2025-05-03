package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.UserLoginModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.*;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.LoginUserRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.RegisterUserRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.RegisterUserResponseDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.UserLoginResponseDTO;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
import com.sena.hidden_pass.infrastructure.services.MailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/hidden_pass/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(userUseCases.getUserById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> registerUser(@Valid @RequestBody RegisterUserRequestDTO user){
        UserModel model = userUseCases.registerUser(new UserModel(
                null,
                new EmailValueObject(user.email()),
                new UsernameValueObject(user.username()),
                user.master_password(),
                user.url_image(),
                null,
                null,
                null,
                null
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponseDTO(
                model.getId_usuario(),
                model.getUsername().getUsername(),
                model.getEmail().getEmail(),
                model.getMaster_password(),
                model.getUrl_image()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@Valid @RequestBody LoginUserRequestDTO user){
        UserLoginModel model = userUseCases.loginUser(new UserModel(
                null,
                new EmailValueObject(user.email()),
                null, user.master_password(),
                null,
                null,
                null,
                null,
                null)
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new UserLoginResponseDTO(
                        model.getUserId(),
                        model.getUsername().getUsername(),
                        model.getEmailValueObject().getEmail(),
                        model.getToken(),
                        model.getUrlImage()
                )
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO user){
        UserModel model = userUseCases.updateUser(id, UserMapper.updateUserDTOToModel(user));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public UserModel recoverMasterPassword(@RequestBody ResetMasterPasswordDTO resetMasterPassword){
        return userUseCases.recoverMasterPassword(resetMasterPassword.getNew_password(), new EmailValueObject(resetMasterPassword.getEmail()));
    }

    @PutMapping("/update/password/{id}")
    public UserModel updateMasterPassword(@PathVariable UUID id, @RequestBody UpdateMasterPassword updateMasterPassword){
        return userUseCases.updateMasterPassword(id, updateMasterPassword.getCurrent_password(), updateMasterPassword.getNew_password());
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable UUID id, @RequestBody DeleteUserDTO deleteUserDTO){
        return userUseCases.deleteUser(id, deleteUserDTO.getCurrent_password());
    }
}
