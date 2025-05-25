package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.UserDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserDBO, UUID> {
    Optional<UserDBO> findByUsername(String username);
    Optional<UserDBO> findByEmail(String email);
}
