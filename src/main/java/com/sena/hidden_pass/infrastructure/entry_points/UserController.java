package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.LoginUserDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.RecoverPasswordDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.ResetMasterPasswordDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;
import com.sena.hidden_pass.infrastructure.mappers.UserMapper;
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
    public UserModel updateUser(@PathVariable UUID id, @RequestBody UserDTO user){
        return userUseCases.updateUser(id, UserMapper.userDTOToModel(user));
    }

    @PutMapping("/update/password")
    public UserModel updatePassword(@RequestBody ResetMasterPasswordDTO resetMasterPassword){
        return userUseCases.updateMasterPassword(resetMasterPassword.getNew_password(), new EmailValueObject(resetMasterPassword.getEmail()));
    }

    @DeleteMapping("/delete/{id}")
    public UserModel deleteUser(@PathVariable UUID id){
        return userUseCases.deleteUser(id);
    }
}
