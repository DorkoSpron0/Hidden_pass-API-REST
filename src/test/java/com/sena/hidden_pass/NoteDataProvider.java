package com.sena.hidden_pass;

import com.sena.hidden_pass.domain.models.NoteModel;
import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;

import java.util.UUID;

public class NoteDataProvider {

    public static NoteModel getNoteModel(){
        return new NoteModel(
                UUID.randomUUID(),
                "Title",
                "Description",
                new PriorityModel(UUID.randomUUID(), PriorityNames.CRITICA)
        );
    }

    public static NoteDBO getNoteDBO(){
        return new NoteDBO(
                UUID.randomUUID(),
                new PriorityDBO(UUID.randomUUID(), PriorityNames.CRITICA),
                "Title",
                "Description"
        );
    }
}
