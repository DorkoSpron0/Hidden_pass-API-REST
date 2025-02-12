package com.sena.hidden_pass.persistence.DBO;


import com.sena.hidden_pass.domain.models.Password;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

    @OneToMany(mappedBy = "id_user")
    private Set<NoteDBO> noteList;

    @OneToMany(mappedBy = "id_user")
    private Set<PasswordDBO> passwordList;

    @OneToMany(mappedBy = "id_user")
    private Set<FolderDBO> folderList;
}
