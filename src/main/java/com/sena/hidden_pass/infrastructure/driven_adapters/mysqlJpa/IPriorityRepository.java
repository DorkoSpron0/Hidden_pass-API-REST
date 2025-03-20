package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.domain.models.PriorityNames;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPriorityRepository extends JpaRepository<PriorityDBO, UUID> {
    Optional<PriorityDBO> getByName(PriorityNames name);
}
