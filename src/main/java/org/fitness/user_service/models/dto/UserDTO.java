package org.fitness.user_service.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.fitness.user_service.models.enums.Role;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String email;
	private String username;
    private String firstName;
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profilePictureUrl;
    private String phoneNumber;
    private List<Role> roles;
}
