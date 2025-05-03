package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.usecases.FolderUseCases;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// TODO - REFACTOR TO DTOS

@RestController
@RequestMapping("/api/v1/hidden_pass/folders")
@AllArgsConstructor
public class FolderController {

    private FolderUseCases folderUseCases;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllFolders(@PathVariable UUID userId) {
        return new ResponseEntity<>(folderUseCases.getAllFolders(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createFolder(@RequestBody FolderModel folderModel, @PathVariable UUID userId) {
        return new ResponseEntity<>(folderUseCases.createFolder(folderModel, userId), HttpStatus.CREATED);
    }

    @PostMapping("/{folderId}/{passwordId}")
    public ResponseEntity<?> setPasswordToFolder(@PathVariable UUID folderId, @PathVariable UUID passwordId) {
        return new ResponseEntity<>(folderUseCases.setPasswordToFolder(folderId, passwordId), HttpStatus.OK);
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<?> getFolderById(@PathVariable UUID folderId){
        return new ResponseEntity<>(folderUseCases.getFolderById(folderId), HttpStatus.OK);
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<?> updateFolder(@RequestBody FolderModel model ,@PathVariable UUID folderId){
        return new ResponseEntity<>(folderUseCases.updateFolder(model, folderId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable UUID folderId){
        return new ResponseEntity<>(folderUseCases.deleteFolder(folderId), HttpStatus.OK);
    }
}
