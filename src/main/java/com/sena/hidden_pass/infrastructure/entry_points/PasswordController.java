package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters.IPasswordAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hidden_pass/passwords")
public class PasswordController {

    private IPasswordAdapter passwordAdapter;

    public PasswordController(IPasswordAdapter iPasswordAdapter){
        this.passwordAdapter = iPasswordAdapter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPasswords(@PathVariable UUID id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.getAllPassword(id));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/password/{id}")
    public ResponseEntity<?> getPasswordById(@PathVariable UUID id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.getPasswordById(id));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // TODO - ADD PASSWORD REGISTRATION



}
