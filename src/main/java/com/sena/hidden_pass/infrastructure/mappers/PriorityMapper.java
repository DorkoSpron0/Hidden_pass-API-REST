package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;

public class PriorityMapper {

    public static PriorityDBO priorityModelToDBO(PriorityModel model){
        return new PriorityDBO(
                model.getId_priority(),
                model.getName()
        );
    }

    public static PriorityModel priorityDBOToModel(PriorityDBO dbo){
        return new PriorityModel(
                dbo.getId_priority(),
                dbo.getName()
        );
    }
}
