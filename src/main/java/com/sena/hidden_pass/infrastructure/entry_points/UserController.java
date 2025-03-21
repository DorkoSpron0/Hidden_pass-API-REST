package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.UserModel;
import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.RecoverPasswordDTO;
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
    public String loginUser(@Valid @RequestBody UserModel user){
        System.out.println(user);
        return userUseCases.loginUser(user);
    }

    @PutMapping("/update/{id}")
    public UserModel updateUser(@PathVariable UUID id, @RequestBody UserModel user){
        return userUseCases.updateUser(id, user);
    }

    @PutMapping("/update/password/{id}")
    public UserModel updatePassword(@PathVariable UUID id, @RequestBody RecoverPasswordDTO password){
        return userUseCases.updateMasterPassword(password.getNew_password(), id);
    }

    @DeleteMapping("/delete/{id}")
    public UserModel deleteUser(@PathVariable UUID id){
        return userUseCases.deleteUser(id);
    }
}
