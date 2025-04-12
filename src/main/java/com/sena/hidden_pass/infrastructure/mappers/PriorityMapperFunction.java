package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;

import java.util.function.Function;

public class PriorityMapperFunction {

    public static final Function<PriorityModel, PriorityDBO> functionModelToDBO = (PriorityModel model) -> new PriorityDBO(
            model.getId_priority(),
            model.getName()
    );

    public static final Function<PriorityDBO, PriorityModel> functionDBOToModel = (PriorityDBO dbo) -> new PriorityModel(
            dbo.getId_priority(),
            dbo.getName()
    );
}
