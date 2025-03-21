package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters.IPasswordAdapter;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.PasswordDTO;
import com.sena.hidden_pass.infrastructure.mappers.PasswordMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}")
    public ResponseEntity<?> createPassword(@Valid @RequestBody PasswordDTO passwordDTO, @PathVariable UUID id){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(passwordAdapter.createPassword(PasswordMapper.passwordDTOToModel(passwordDTO), id));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, @PathVariable UUID id){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(passwordAdapter.editPassword(PasswordMapper.passwordDTOToModel(passwordDTO), id));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/password/{id}")
    public ResponseEntity<?> deletePassword(@PathVariable UUID id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.deletePassword(id));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
