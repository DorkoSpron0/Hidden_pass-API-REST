package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.usecases.UserUseCases;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/hidden_pass/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping("/{id}")
    public UserDBO getUserById(@PathVariable UUID id){
        return userUseCases.getUserById(id);
    }

    @PostMapping("/register")
    public UserDBO registerUser(@RequestBody UserDTO userDTO){
        return userUseCases.registerUser(userDTO.toDomain());
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO){
        return userUseCases.loginUser(userDTO.toDomain());
    }

    @PutMapping("/update/{id}")
    public UserDBO updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO){
        return userUseCases.updateUser(id, userDTO.toDomain());
    }

    @DeleteMapping("/delete/{id}")
    public UserDBO deleteUser(@PathVariable UUID id){
        return userUseCases.deleteUser(id);
    }
}
