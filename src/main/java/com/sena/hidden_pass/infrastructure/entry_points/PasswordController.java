package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
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

    private PasswordUseCases passwordAdapter;

    public PasswordController(PasswordUseCases iPasswordAdapter){
        this.passwordAdapter = iPasswordAdapter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPasswords(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.getAllPassword(id));
    }

    @GetMapping("/password/{id}")
    public ResponseEntity<?> getPasswordById(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.getPasswordById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createPassword(@Valid @RequestBody PasswordDTO passwordDTO, @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordAdapter.createPassword(PasswordMapper.passwordDTOToModel(passwordDTO), id));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordAdapter.editPassword(PasswordMapper.passwordDTOToModel(passwordDTO), id));
    }

    @DeleteMapping("/password/{id}")
    public ResponseEntity<?> deletePassword(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.deletePassword(id));
    }
}
