package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NoteDTO {

    private PriorityNames priorityName;
    private String title;
    private String description;

    @Override
    public String toString() {
        return "NoteDTO{" +
                "description='" + description + '\'' +
                ", priorityName=" + priorityName +
                ", title='" + title + '\'' +
                '}';
    }
}
