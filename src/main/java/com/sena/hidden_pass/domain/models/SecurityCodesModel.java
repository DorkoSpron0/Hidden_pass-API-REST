package com.sena.hidden_pass.domain.models;

import java.util.UUID;

public class SecurityCodesModel {

    private UUID security_code;

    public SecurityCodesModel(UUID security_code) {
        this.security_code = security_code;
    }

    public UUID getSecurity_code() {
        return security_code;
    }

    public void setSecurity_code(UUID security_code) {
        this.security_code = security_code;
    }

    @Override
    public String toString() {
        return "SecurityCodesModel{" +
                "security_code=" + security_code +
                '}';
    }
}
