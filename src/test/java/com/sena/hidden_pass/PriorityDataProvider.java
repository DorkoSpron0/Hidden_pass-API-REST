package com.sena.hidden_pass;

import com.sena.hidden_pass.domain.models.PriorityModel;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;

import java.util.UUID;

public class PriorityDataProvider {

    public static PriorityModel getPriorityModel(){
        return new PriorityModel(
                UUID.randomUUID(),
                PriorityNames.CRITICA
        );
    }

    public static PriorityDBO getPriorityDBO(){
        return new PriorityDBO(
                UUID.randomUUID(),
                PriorityNames.CRITICA
        );
    }
}
