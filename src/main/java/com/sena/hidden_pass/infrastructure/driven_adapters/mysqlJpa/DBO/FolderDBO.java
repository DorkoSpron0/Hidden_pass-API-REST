package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import jakarta.persistence.*;
import lombok.*;

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
}
