package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.NoteDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface INoteRepository extends JpaRepository<NoteDBO, UUID> {
}
