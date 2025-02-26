package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters.ISecurityCodesAdapter;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.SendSecurityCodeDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
