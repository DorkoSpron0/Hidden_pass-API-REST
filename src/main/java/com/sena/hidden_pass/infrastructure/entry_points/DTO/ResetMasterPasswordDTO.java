package com.sena.hidden_pass.infrastructure.entry_points.DTO;

public class ResetMasterPasswordDTO {

    private String email;
    private String new_password;

    public ResetMasterPasswordDTO() {
    }

    public ResetMasterPasswordDTO(String email, String new_password) {
        this.email = email;
        this.new_password = new_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
