package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import com.sena.hidden_pass.domain.models.PriorityNames;
import jakarta.persistence.*;
import lombok.*;

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

    public PriorityDBO(PriorityNames name) {
        this.name = name;
    }
}
