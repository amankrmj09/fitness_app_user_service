package org.fitness.user_service.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
public class LoginRequestDTO {
    private String usernameOrEmail;
    private String password;
}
