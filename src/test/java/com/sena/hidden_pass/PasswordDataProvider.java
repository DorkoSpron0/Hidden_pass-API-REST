package com.sena.hidden_pass;

import com.sena.hidden_pass.domain.models.FolderModel;
import com.sena.hidden_pass.domain.models.PasswordModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.FolderDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.PasswordDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordDataProvider {

    public static PasswordModel getPasswordModel(){
        return new PasswordModel(
                UUID.randomUUID(),
                "Name",
                "Description",
                "test@test.com",
                "Password123@",
                "http://localhost:3306.com",
                LocalDateTime.now(),
                new FolderModel(UUID.randomUUID(), "Title", "Description", "icon.png")
        );
    }

    public static PasswordDBO getPasswordDBO(){
        return new PasswordDBO(
                UUID.randomUUID(),
                "Name",
                "http://localhost:3306.com",
                LocalDateTime.now(),
                "test@test.com",
                "Passwrod123@",
                "Description",
                new FolderDBO(UUID.randomUUID(), "Name", "icon.png", "Description")
        );
    }

    public static PasswordDTO getPasswordDTO(){
        return new PasswordDTO(
                "Name",
                "HttP::Localhost.com",
                LocalDateTime.now(),
                "test@test.com",
                "Passwird",
                "description",
                new FolderDBO(UUID.randomUUID(), "Name", "icon.png", "Description")
        );
    }
}
