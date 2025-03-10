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

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserDBO userDBO;

    public SecurityCodesDBO(UserDBO userDBO) {
        this.userDBO = userDBO;
    }

    @Override
    public String toString() {
        return "SecurityCodesDBO{" +
                "security_code=" + security_code +
                ", userDBO=" + userDBO +
                '}';
    }
}
