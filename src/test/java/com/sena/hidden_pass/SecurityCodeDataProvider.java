package com.sena.hidden_pass;

import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;

import java.util.UUID;

public class SecurityCodeDataProvider {

    public static SecurityCodesModel getSecurityCodesModel(){
        return new SecurityCodesModel(UUID.randomUUID());
    }

    public static SecurityCodesDBO getSecurityCodesDBO(){
        return new SecurityCodesDBO(UUID.randomUUID());
    }
}
