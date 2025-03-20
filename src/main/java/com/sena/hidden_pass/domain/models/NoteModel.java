package com.sena.hidden_pass.domain.models;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;

import java.util.UUID;

public class NoteModel {

    private UUID id_note;
    private PriorityModel id_priority;
    private String title;
    private String description;

    public NoteModel() {
    }

    public NoteModel(UUID id_note, String title, String description, PriorityModel id_priority) {
        this.id_note = id_note;
        this.title = title;
        this.description = description;
        this.id_priority = id_priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId_note() {
        return id_note;
    }

    public void setId_note(UUID id_note) {
        this.id_note = id_note;
    }

    public PriorityModel getId_priority() {
        return id_priority;
    }

    public void setId_priority(PriorityModel id_priority) {
        this.id_priority = id_priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NoteModel{" +
                "description='" + description + '\'' +
                ", id_note=" + id_note +
                ", id_priority=" + id_priority +
                ", title='" + title + '\'' +
                '}';
    }
}
