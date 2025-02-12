package com.sena.hidden_pass.domain.models;

import java.util.UUID;

public class Note {

    private UUID id_note;
    private Priority priority;
    private UserModel id_user;
    private String title;
    private String description;

    public Note() {
    }

    public Note(String description, UUID id_note, UserModel id_user, Priority priority, String title) {
        this.description = description;
        this.id_note = id_note;
        this.id_user = id_user;
        this.priority = priority;
        this.title = title;
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

    public UserModel getId_user() {
        return id_user;
    }

    public void setId_user(UserModel id_user) {
        this.id_user = id_user;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Note{" +
                "description='" + description + '\'' +
                ", id_note=" + id_note +
                ", priority=" + priority +
                ", id_user=" + id_user +
                ", title='" + title + '\'' +
                '}';
    }
}
