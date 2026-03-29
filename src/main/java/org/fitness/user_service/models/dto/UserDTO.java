package org.fitness.user_service.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String phoneNumber;
}
