package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.domain.usecases.PasswordUseCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.PasswordRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.PasswordInfoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/hidden_pass/passwords")
public class PasswordController {

    private PasswordUseCases passwordAdapter;

    public PasswordController(PasswordUseCases iPasswordAdapter){
        this.passwordAdapter = iPasswordAdapter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<PasswordInfoResponseDTO>> getPasswords(@PathVariable UUID id){
        Set<PasswordModel> passwords = passwordAdapter.getAllPassword(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                passwords.stream().map(
                        this::modelToDTO
                ).collect(Collectors.toSet())
        );
    }

    @GetMapping("/password/{id}")
    public ResponseEntity<PasswordInfoResponseDTO> getPasswordById(@PathVariable UUID id){

        PasswordModel model = passwordAdapter.getPasswordById(id);

        return ResponseEntity.status(HttpStatus.OK).body(this.modelToDTO(model));
    }

    @PostMapping("/{id}")
    public ResponseEntity<PasswordInfoResponseDTO> createPassword(@Valid @RequestBody PasswordRequestDTO passwordDTO, @PathVariable UUID id){

        PasswordModel model = passwordAdapter.createPassword(new PasswordModel(
                null,
                passwordDTO.name(),
                passwordDTO.description(),
                passwordDTO.email_user(),
                passwordDTO.password(),
                passwordDTO.url(),
                passwordDTO.dateTime(),
                null
        ), id, passwordDTO.folder_name());

        return ResponseEntity.status(HttpStatus.CREATED).body(this.modelToDTO(model));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<PasswordInfoResponseDTO> updatePassword(@Valid @RequestBody PasswordRequestDTO passwordDTO, @PathVariable UUID id){
        PasswordModel model = passwordAdapter.editPassword(
                new PasswordModel(
                        null,
                        passwordDTO.name(),
                        passwordDTO.description(),
                        passwordDTO.email_user(),
                        passwordDTO.password(),
                        passwordDTO.url(),
                        passwordDTO.dateTime(),
                        null
                ),
                id,
                passwordDTO.folder_name()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(this.modelToDTO(model));
    }

    @DeleteMapping("/password/{id}")
    public ResponseEntity<String> deletePassword(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(passwordAdapter.deletePassword(id));
    }


    public PasswordInfoResponseDTO modelToDTO(PasswordModel model){
        return new PasswordInfoResponseDTO(
                model.getId_password(),
                model.getName(),
                model.getUrl(),
                model.getDateTime(),
                model.getEmail_user(),
                model.getPassword(),
                model.getDescription(),
                model.getId_folder() != null ? model.getId_folder().getId_folder() : null
        );
    }
}
