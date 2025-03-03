package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "user")
public class UserDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_usuario;

    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "master_password", nullable = false)
    private String master_password;

    private String url_image;

    @OneToMany(mappedBy = "id_user")
    private Set<NoteDBO> noteList;

    @OneToMany(mappedBy = "id_user")
    private Set<PasswordDBO> passwordList;

    @OneToMany(mappedBy = "id_user")
    private Set<FolderDBO> folderList;

    @OneToMany(mappedBy = "security_code")
    private Set<SecurityCodesDBO> securityCodes;

    public UserDBO(String email, Set<FolderDBO> folderList, String master_password, Set<NoteDBO> noteList, Set<PasswordDBO> passwordList, String username, Set<SecurityCodesDBO> securityCodes, String url_image) {
        this.email = email;
        this.folderList = folderList;
        this.master_password = master_password;
        this.noteList = noteList;
        this.passwordList = passwordList;
        this.username = username;
        this.securityCodes = securityCodes;
        this.url_image = url_image;
    }

}
