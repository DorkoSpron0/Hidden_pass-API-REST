package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.usecases.SecurityCodesCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.SendSecurityCodeDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.ValidateSecurityCodeDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/hidden_pass/codes")
public class SecurityCodesController {

    private SecurityCodesCases securityCodesAdapter;

    @PostMapping("/send")
    public String sendSecurityCode(@Valid @RequestBody SendSecurityCodeDTO sendSecurityCode) throws MessagingException {
            return securityCodesAdapter.sendSecurityCode(sendSecurityCode.getEmail());
    }

    @PostMapping("/validate")
    public String validateSecurityCode(@RequestBody ValidateSecurityCodeDTO securityCode){
        boolean isValid = securityCodesAdapter.validateSecurityCode(UUID.fromString(securityCode.getSecurityCode()), securityCode.getEmail());

        if(isValid) return "VALID";
        return "NO VALID";
    }
}
