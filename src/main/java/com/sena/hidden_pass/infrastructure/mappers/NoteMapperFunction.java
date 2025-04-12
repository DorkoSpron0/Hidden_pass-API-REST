package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.NoteDTO;

import java.util.function.Function;

public class NoteMapperFunction {

    public static final Function<NoteModel, NoteDBO> functionModelToDBO = (NoteModel model) ->
            new NoteDBO(
                    model.getId_note(),
                    model.getId_priority() != null ? new PriorityDBO(
                            model.getId_priority().getName()
                    ) : null,
                    model.getTitle(),
                    model.getDescription()
            );

    public static final Function<NoteDBO, NoteModel> functionDBOToModel = (NoteDBO dbo) -> new NoteModel(
            dbo.getId_note(),
            dbo.getTitle(),
            dbo.getDescription(),
            dbo.getId_priority() != null ? new PriorityModel(
                    dbo.getId_priority().getId_priority(),
                    dbo.getId_priority().getName()
            ) : null
    );

    public static final Function<NoteDTO, NoteModel> functionDTOToModel = (NoteDTO dto) -> new NoteModel(
            null,
            dto.getTitle(),
            dto.getDescription(),
            dto.getPriorityName() != null ? new PriorityModel(
                    null,
                    dto.getPriorityName()
            ) : null
    );
}
