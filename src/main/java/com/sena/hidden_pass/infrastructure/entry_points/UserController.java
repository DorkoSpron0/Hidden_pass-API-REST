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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/hidden_pass/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    private MailService mailService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userUseCases.getUserById(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){
        try{
            mailService.sendEmailAyncImpl(user.getEmail().getEmail(), "BIENVENIDO A HIDDEN PASS", "Bienvenido <3");
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
    public UserModel updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO user){
        System.out.println(user.toString());
        return userUseCases.updateUser(id, UserMapper.updateUserDTOToModel(user), user.getMaster_password_saved().getMaster_password());
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
