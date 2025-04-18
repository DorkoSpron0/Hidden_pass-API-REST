package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PasswordDTO {

    @Pattern(regexp = "^[A-Za-z\\s\\d]{4,}$")
    private String name;
    private String url;
    private LocalDateTime dateTime;
    private String email_user;
    private String password;
    private String description;
    private FolderDBO id_folder;
}
