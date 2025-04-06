package com.sena.hidden_pass.infrastructure.entry_points.DTO;

public class UpdateMasterPassword {

    private String current_password;
    private String new_password;

    public UpdateMasterPassword() {
    }

    public UpdateMasterPassword(String current_password, String new_password) {
        this.current_password = current_password;
        this.new_password = new_password;
    }

    public String getCurrent_password() {
        return current_password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @Override
    public String toString() {
        return "UpdateMasterPassword{" +
                "current_password='" + current_password + '\'' +
                ", new_password='" + new_password + '\'' +
                '}';
    }
}
