package com.sena.hidden_pass.infrastructure.entry_points;

import com.sena.hidden_pass.domain.usecases.SecurityCodesCases;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.SendSecurityCodeRequestDTO;
import com.sena.hidden_pass.infrastructure.entry_points.DTO.request.ValidateSecurityCodeRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> sendSecurityCode(@Valid @RequestBody SendSecurityCodeRequestDTO sendSecurityCode) throws MessagingException {

        String result = securityCodesAdapter.sendSecurityCode(sendSecurityCode.email());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateSecurityCode(@RequestBody ValidateSecurityCodeRequestDTO securityCode){
        boolean isValid = securityCodesAdapter.validateSecurityCode(UUID.fromString(securityCode.securityCode()), securityCode.email());

        if(isValid) return ResponseEntity.status(HttpStatus.OK).body("VALID");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO VALID");
    }
}
