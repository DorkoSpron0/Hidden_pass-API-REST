package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters.ISecurityCodesAdapter;
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

    private ISecurityCodesAdapter securityCodesAdapter;

    @PostMapping("/send")
    public String sendSecurityCode(@Valid @RequestBody SendSecurityCodeDTO sendSecurityCode) {

        try{
            return securityCodesAdapter.sendSecurityCode(sendSecurityCode.getEmail());
        }catch (MessagingException ex){
            return ex.getMessage();
        }

    }

    @PostMapping("/validate")
    public String validateSecurityCode(@RequestBody ValidateSecurityCodeDTO securityCode){
        boolean isValid = securityCodesAdapter.validateSecurityCode(UUID.fromString(securityCode.getSecurityCode()), securityCode.getEmail());

        if(isValid) return "VALID";
        return "NO VALID";
    }
}
