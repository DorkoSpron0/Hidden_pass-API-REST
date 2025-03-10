package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"}))
public class UserDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_usuario;

    @Embedded
    private UsernameValueObject username;

    @Embedded
    private EmailValueObject email;

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

    public UserDBO(EmailValueObject email, Set<FolderDBO> folderList, String master_password, Set<NoteDBO> noteList, Set<PasswordDBO> passwordList, UsernameValueObject username, Set<SecurityCodesDBO> securityCodes, String url_image) {
        this.email = email;
        this.folderList = folderList;
        this.master_password = master_password;
        this.noteList = noteList;
        this.passwordList = passwordList;
        this.username = username;
        this.securityCodes = securityCodes;
        this.url_image = url_image;
    }

    @Override
    public String toString() {
        return "UserDBO{" +
                "email=" + email +
                ", id_usuario=" + id_usuario +
                ", username=" + username +
                ", master_password='" + master_password + '\'' +
                ", url_image='" + url_image + '\'' +
                ", noteList=" + noteList +
                ", passwordList=" + passwordList +
                ", folderList=" + folderList +
                ", securityCodes=" + securityCodes +
                '}';
    }
}
