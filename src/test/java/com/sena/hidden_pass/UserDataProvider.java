package com.sena.hidden_pass;

import com.sena.hidden_pass.domain.models.*;
import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserDataProvider {

    public static UserModel getUserModel(){
        return new UserModel(
                UUID.randomUUID(),
                new EmailValueObject("nicky@nicky.com"),
                new UsernameValueObject("username"),
                "master_password",
                "http://image.co",
                Set.of(new PasswordModel(
                        UUID.randomUUID(),
                        "passwordname",
                        "description1",
                        "test@test.com",
                        "Password1",
                        "http://google.test",
                        LocalDateTime.now(),
                        new FolderModel(UUID.randomUUID(), "FolderName", "DescriptionFolder", "icon.png"))
                ),

                Set.of(new NoteModel(
                        UUID.randomUUID(),
                        "titleNote",
                        "DescrtiptionNote",
                        new PriorityModel(UUID.randomUUID(), PriorityNames.CRITICA))
                ),

                Set.of(new FolderModel(
                        UUID.randomUUID(),
                        "folderName",
                        "descriptionFolder",
                        "icon.png")
                ),

                new SecurityCodesModel(UUID.randomUUID())
        );
    }

    public static UserDBO getUserDBO(){
        return new UserDBO(
                UUID.randomUUID(),
                "username",
                "nicky@nicky.com",
                "master_password",
                "http://image.co",
                Set.of(new NoteDBO(
                        UUID.randomUUID(),
                        new PriorityDBO(UUID.randomUUID(), PriorityNames.ALTA),
                        "title",
                        "description")
                ),

                Set.of(new PasswordDBO(
                        UUID.randomUUID(),
                        "name",
                        "url",
                        LocalDateTime.now() ,
                        "email",
                        "password",
                        "descirption",
                        new FolderDBO(UUID.randomUUID(), "folderName", "icon.png", "DescriptionFolder"))
                ),

                new HashSet<>(Set.of(new FolderDBO(
                        UUID.randomUUID(),
                        "folderName",
                        "icon.png",
                        "descriptionFolder"
                        )
                )),

                new SecurityCodesDBO(UUID.randomUUID())
        );
    }
}
