package com.sena.hidden_pass.infrastructure.entry_points.DTO;

public class DeleteUserDTO {

    private String current_password;

    public DeleteUserDTO() {
    }

    public DeleteUserDTO(String current_password) {
        this.current_password = current_password;
    }

    public String getCurrent_password() {
        return current_password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    @Override
    public String toString() {
        return "DeleteUserDTO{" +
                "current_password='" + current_password + '\'' +
                '}';
    }
}
