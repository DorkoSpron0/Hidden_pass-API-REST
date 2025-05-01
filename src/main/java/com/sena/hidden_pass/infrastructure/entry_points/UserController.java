package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.ResetMasterPasswordDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UpdateMasterPassword;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UpdateUserDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(userUseCases.registerUser(UserMapper.userDTOToModel(user)));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody UserDTO user){
        return userUseCases.loginUser(UserMapper.userDTOToModel(user));
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
    public UserModel deleteUser(@PathVariable UUID id){
        return userUseCases.deleteUser(id);
    }
}
