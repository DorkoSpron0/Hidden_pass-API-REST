package com.sena.hidden_pass.infrastructure.mappers;

import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;

public class SecurityCodeMapper {
    public static SecurityCodesDBO securityCodeModelToDBO(SecurityCodesModel model){
        return new SecurityCodesDBO(
                model.getSecurity_code()
        );
    }

    public static SecurityCodesModel securityCodesDBOToModel(SecurityCodesDBO dbo){
        return new SecurityCodesModel(
                dbo.getSecurity_code()
        );
    }

    public boolean passTest(){
        return true;
    }
}
