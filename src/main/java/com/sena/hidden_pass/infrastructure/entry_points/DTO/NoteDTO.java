package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
public class NoteDTO {

    private PriorityNames priorityName;
    private UserDBO id_user;
    private String title;
    private String description;

    public NoteDBO toDomain(IPriorityRepository priorityRepository){
        PriorityDBO priority =  priorityRepository.getByName(priorityName).orElseThrow(() -> new IllegalArgumentException(""));

        return NoteDBO.builder()
                .id_priority(priority)
                .id_user(id_user)
                .title(title)
                .description(description)
                .build();
    }
}
