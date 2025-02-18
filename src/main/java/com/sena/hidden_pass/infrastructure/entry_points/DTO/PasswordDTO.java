package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PasswordDTO {
    private UUID id_password;
    private UserDBO id_user;
    private String name;
    private String url;
    private LocalDateTime dateTime;
    private String email_user;
    private String password;
    private String description;
    private FolderDBO id_folder;
}
