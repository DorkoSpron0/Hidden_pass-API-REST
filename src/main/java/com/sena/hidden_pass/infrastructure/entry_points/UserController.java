package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.UserLoginModel;
import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.*;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.RegisterUserResponseDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.UserInfoResponseDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.UserLoginResponseDTO;
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
    public ResponseEntity<UserInfoResponseDTO> getUserById(@PathVariable UUID id){
        UserModel model = userUseCases.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponseDTO(
                model.getId_usuario(),
                model.getUsername().getUsername(),
                model.getEmail().getEmail(),
                model.getMaster_password(),
                model.getUrl_image()
        ));
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
    public ResponseEntity<UserInfoResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequestDTO user){

        UserModel model = userUseCases.updateUser(id, new UserModel(
                null,
                new EmailValueObject(user.email()),
                new UsernameValueObject(user.username()),
                null,
                user.url_image(),
                null,
                null,
                null,
                null
        ));

        return new ResponseEntity<>(new UserInfoResponseDTO(
                model.getId_usuario(),
                model.getUsername().getUsername(),
                model.getEmail().getEmail(),
                model.getMaster_password(),
                model.getUrl_image()
        ), HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<UserInfoResponseDTO> recoverMasterPassword(@RequestBody ResetMasterPasswordRequestDTO resetMasterPassword){

        UserModel model = userUseCases.recoverMasterPassword(resetMasterPassword.new_password(), new EmailValueObject(resetMasterPassword.email()));

        return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponseDTO(
                model.getId_usuario(),
                model.getUsername().getUsername(),
                model.getEmail().getEmail(),
                model.getMaster_password(),
                model.getUrl_image()
        ));
    }

    @PutMapping("/update/password/{id}")
    public ResponseEntity<UserInfoResponseDTO> updateMasterPassword(@PathVariable UUID id, @Valid @RequestBody UpdateMasterPasswordRequestDTO updateMasterPassword){

        UserModel model = userUseCases.updateMasterPassword(id, updateMasterPassword.current_password(), updateMasterPassword.new_password());

        return ResponseEntity.status(HttpStatus.OK).body(new UserInfoResponseDTO(
                model.getId_usuario(),
                model.getUsername().getUsername(),
                model.getEmail().getEmail(),
                model.getMaster_password(),
                model.getUrl_image()
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id, @RequestBody DeleteUserRequestDTO deleteUserDTO){

        String result = userUseCases.deleteUser(id, deleteUserDTO.current_password());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
