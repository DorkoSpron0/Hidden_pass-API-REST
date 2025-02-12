package com.sena.hidden_pass.persistence.DBO;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "priority")
public class PriorityDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_priority;

    @Enumerated(value = EnumType.STRING)
    private PriorityNames name;

    @OneToMany(mappedBy = "id_priority")
    private List<NoteDBO> noteList;
}
