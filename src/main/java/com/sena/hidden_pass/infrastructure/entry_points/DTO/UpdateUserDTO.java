package com.sena.hidden_pass.infrastructure.entry_points.DTO;

import com.sena.hidden_pass.domain.valueObjects.EmailValueObject;
import com.sena.hidden_pass.domain.valueObjects.MasterPasswordValueObject;
import com.sena.hidden_pass.domain.valueObjects.UsernameValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserDTO {

    private UsernameValueObject username;
    private EmailValueObject email;
    private String url_image;

    @Override
    public String toString() {
        return "UpdateUserDTO{" +
                "email=" + email +
                ", username=" + username +
                ", url_image='" + url_image + '\'' +
                '}';
    }
}
