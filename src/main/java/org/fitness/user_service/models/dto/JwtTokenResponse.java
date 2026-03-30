package org.fitness.user_service.models.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JwtTokenResponse {
    private String username;
    private String token;
    private String refreshToken;
    private String tokenType;
    private String validUntil;
}
