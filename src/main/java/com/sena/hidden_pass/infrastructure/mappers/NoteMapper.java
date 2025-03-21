package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.NoteDTO;

public class NoteMapper {
    public static NoteDBO noteModelToDBO(NoteModel model){
        return new NoteDBO(
                model.getId_note(),
                model.getId_priority() != null ?
                    new PriorityDBO(
                            model.getId_priority().getId_priority(),
                            model.getId_priority().getName()
                    ) : null,
                model.getTitle(),
                model.getDescription()
        );
    }

    public static NoteModel noteDBOToModel(NoteDBO dbo){
        return new NoteModel(
                dbo.getId_note(),
                dbo.getTitle(),
                dbo.getDescription(),
                dbo.getId_priority() != null ?
                        new PriorityModel(dbo.getId_priority().getId_priority(), dbo.getId_priority().getName()) : null
        );
    }

    public static NoteModel noteDTOToModel(NoteDTO dto){
        return new NoteModel(
                null,
                dto.getTitle(),
                dto.getDescription(),
                new PriorityModel(null, dto.getPriorityName())
        );
    }
}
