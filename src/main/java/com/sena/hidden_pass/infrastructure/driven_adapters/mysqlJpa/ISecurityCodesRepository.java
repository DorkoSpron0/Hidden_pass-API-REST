package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.SecurityCodesDBO;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ISecurityCodesRepository extends JpaRepository<SecurityCodesDBO, UUID> {
}
