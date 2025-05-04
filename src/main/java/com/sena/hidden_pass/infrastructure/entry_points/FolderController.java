package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.usecases.FolderUseCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.FolderRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.FolderInfoResponseDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.response.PasswordInfoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hidden_pass/folders")
public class FolderController {

    private FolderUseCases folderUseCases;

    public FolderController(FolderUseCases folderUseCases) {
        this.folderUseCases = folderUseCases;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FolderInfoResponseDTO>> getAllFolders(@PathVariable UUID userId) {

        List<FolderModel> model = folderUseCases.getAllFolders(userId);

        return ResponseEntity.status(HttpStatus.OK).body(model.stream()
                .map(this::modelToDTO).toList()
        );
    }

    @PostMapping("/{userId}")
    public ResponseEntity<FolderInfoResponseDTO> createFolder(@RequestBody FolderRequestDTO folder, @PathVariable UUID userId) {

        FolderModel model = folderUseCases.createFolder(new FolderModel(
                folder.description(),
                folder.icon(),
                null,
                folder.name(),
                null,
                null
        ), userId, folder.passwords());

        return new ResponseEntity<>(this.modelToDTO(model), HttpStatus.CREATED);
    }

    @PostMapping("/{folderId}/{passwordId}")
    public ResponseEntity<FolderInfoResponseDTO> setPasswordToFolder(@PathVariable UUID folderId, @PathVariable UUID passwordId) {

        FolderModel model = folderUseCases.setPasswordToFolder(folderId, passwordId);

        return new ResponseEntity<>(this.modelToDTO(model), HttpStatus.OK);
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<FolderInfoResponseDTO> getFolderById(@PathVariable UUID folderId){

        FolderModel model = folderUseCases.getFolderById(folderId);

        return ResponseEntity.status(HttpStatus.OK).body(this.modelToDTO(model));
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<FolderInfoResponseDTO> updateFolder(@RequestBody FolderRequestDTO model ,@PathVariable UUID folderId){

        FolderModel folder = folderUseCases.updateFolder(new FolderModel(
                model.description(),
                model.icon(),
                null,
                model.name(),
                null,
                null
        ), folderId, model.passwords());

        return new ResponseEntity<>(this.modelToDTO(folder), HttpStatus.CREATED);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<String> deleteFolder(@PathVariable UUID folderId){
        return new ResponseEntity<>(folderUseCases.deleteFolder(folderId), HttpStatus.OK);
    }


    public FolderInfoResponseDTO modelToDTO(FolderModel model){
        return new FolderInfoResponseDTO(
                model.getId_folder(),
                model.getName(),
                model.getIcon(),
                model.getDescription(),

                model.getPasswordModels() != null ? model.getPasswordModels().stream()
                        .map(passwordModel -> new PasswordInfoResponseDTO(
                                passwordModel.getId_password(),
                                passwordModel.getName(),
                                passwordModel.getUrl(),
                                passwordModel.getDateTime(),
                                passwordModel.getEmail_user(),
                                passwordModel.getPassword(),
                                passwordModel.getDescription(),
                                passwordModel.getId_folder() != null ? passwordModel.getId_folder().getId_folder() : null
                        )).toList() : new ArrayList<>()
        );
    }
}
