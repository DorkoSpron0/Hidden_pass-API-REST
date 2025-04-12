package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;

import java.util.function.Function;

public class SecurityCodeMapperFunction {

    public static final Function<SecurityCodesModel, SecurityCodesDBO> functionModelToDBO = (SecurityCodesModel model) -> new SecurityCodesDBO(
            model.getSecurity_code()
    );

    public static SecurityCodesModel securityCodesDBOToModel(SecurityCodesDBO dbo){
        return new SecurityCodesModel(
                dbo.getSecurity_code()
        );
    }

    public static final Function<SecurityCodesDBO, SecurityCodesModel> functionDBOToModel = (SecurityCodesDBO dbo) -> new SecurityCodesModel(
            dbo.getSecurity_code()
    );
}
