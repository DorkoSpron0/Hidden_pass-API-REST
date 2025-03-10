package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateSecurityCodeDTO {
    private String email;
    private String securityCode;

}
