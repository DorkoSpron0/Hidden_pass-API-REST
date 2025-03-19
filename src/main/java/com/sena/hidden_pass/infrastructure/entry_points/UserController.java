package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.LoginUserDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.RecoverPasswordDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(userUseCases.registerUser(userDTO.toDomain()));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody LoginUserDTO loginUserDTO){
        System.out.println(loginUserDTO.toString());
        return userUseCases.loginUser(loginUserDTO.toDomain());
    }

    @PutMapping("/update/{id}")
    public UserDBO updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO){
        return userUseCases.updateUser(id, userDTO.toDomain());
    }

    @PutMapping("/update/password/{id}")
    public UserDBO updatePassword(@PathVariable UUID id, @RequestBody RecoverPasswordDTO password){
        return userUseCases.updateMasterPassword(password.getNew_password(), id);
    }

    @DeleteMapping("/delete/{id}")
    public UserDBO deleteUser(@PathVariable UUID id){
        return userUseCases.deleteUser(id);
    }
}
