package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PasswordDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IPasswordRepository extends JpaRepository<PasswordDBO, UUID> {
    List<PasswordDBO> findPasswordDBOByNameIn(List<String> passwordsNames);
}
