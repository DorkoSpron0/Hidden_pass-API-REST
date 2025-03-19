package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "password")
public class PasswordDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_password;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "id_user", nullable = false)
//    private UserDBO id_user;
    private String name;
    private String url;
    private LocalDateTime dateTime;
    private String email_user;
    private String password;
    private String description;
    @ManyToOne
    @JoinColumn(name = "id_folder", nullable = true)
    private FolderDBO id_folder;

    @Override
    public String toString() {
        return "PasswordDBO{" +
                "url='" + url + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", id_password=" + id_password +
                ", id_folder=" + id_folder +
                ", email_user='" + email_user + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
