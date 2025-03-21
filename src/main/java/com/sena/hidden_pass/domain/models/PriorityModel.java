package com.sena.hidden_pass.domain.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

public class PriorityModel {

    private UUID id_priority;
    private PriorityNames name;

    public PriorityModel(UUID id_priority, PriorityNames name) {
        this.id_priority = id_priority;
        this.name = name;
    }

    public UUID getId_priority() {
        return id_priority;
    }

    public void setId_priority(UUID id_priority) {
        this.id_priority = id_priority;
    }

    public PriorityNames getName() {
        return name;
    }

    public void setName(PriorityNames name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PriorityModel{" +
                "id_priority=" + id_priority +
                ", name=" + name +
                '}';
    }
}
