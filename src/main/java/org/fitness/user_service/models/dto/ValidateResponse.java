package org.fitness.user_service.models.dto;

import java.util.List;

public record ValidateResponse(String userId, List<String> roles) {

}
