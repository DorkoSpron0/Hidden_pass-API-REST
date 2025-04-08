package com.sena.hidden_pass.domain.usecases;

import com.sena.hidden_pass.domain.models.SecurityCodesModel;
import jakarta.mail.MessagingException;

import java.util.UUID;

public interface SecurityCodesCases {
    String sendSecurityCode(String email) throws MessagingException;
    SecurityCodesModel getSecurityCode();
    boolean validateSecurityCode(UUID security_code, String user);
}
