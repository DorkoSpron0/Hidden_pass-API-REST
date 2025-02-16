package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "note")
public class NoteDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_note;

    @ManyToOne
    @JoinColumn(name="id_priority", nullable=false)
    private PriorityDBO id_priority;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserDBO id_user;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false, unique = true)
    private String description;
}
