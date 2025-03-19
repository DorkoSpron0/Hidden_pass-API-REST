package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "SecurityCodes")
public class SecurityCodesDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID security_code;


    @Override
    public String toString() {
        return "SecurityCodesDBO{" +
                "security_code=" + security_code +
                '}';
    }
}
