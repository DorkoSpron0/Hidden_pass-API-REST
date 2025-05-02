package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "folder")
public class FolderDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_folder;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    private String icon;
    private String Description;

    @ManyToOne
    @JoinColumn(name = "id_folder", referencedColumnName = "id_folder")
    private UserDBO user;

    @OneToMany
    List<PasswordDBO> passwords = new ArrayList<>();

    public FolderDBO(UUID id_folder, String name, String icon, String description) {
        this.id_folder = id_folder;
        this.name = name;
        this.icon = icon;
        Description = description;
    }


}
