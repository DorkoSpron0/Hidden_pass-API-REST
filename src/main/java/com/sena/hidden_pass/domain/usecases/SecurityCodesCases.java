package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import jakarta.mail.MessagingException;

public interface SecurityCodesCases {
    String sendSecurityCode(String email) throws MessagingException;
    SecurityCodesDBO getSecurityCode();
}
