package com.sena.hidden_pass.infrastructure.entry_points.DTO;

public class RecoverPasswordDTO {

    private String new_password;

    public RecoverPasswordDTO() {
    }

    public RecoverPasswordDTO(String new_password) {
        this.new_password = new_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @Override
    public String toString() {
        return "RecoverPasswordDTO{" +
                "new_password='" + new_password + '\'' +
                '}';
    }
}
