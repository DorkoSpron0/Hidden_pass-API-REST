package com.sena.hidden_pass.domain.models;

import java.util.UUID;

public class Priority {
    private UUID id_priority;
    private String name;

    public Priority() {
    }

    public Priority(UUID id_priority, String name) {
        this.id_priority = id_priority;
        this.name = name;
    }

    public UUID getId_priority() {
        return id_priority;
    }

    public void setId_priority(UUID id_priority) {
        this.id_priority = id_priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Priority{" +
                "id_priority=" + id_priority +
                ", name='" + name + '\'' +
                '}';
    }
}
