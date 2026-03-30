package org.fitness.user_service.models.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeleteResponseDTO {
    private String message;
    private LocalDateTime timestamp;

    public DeleteResponseDTO(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
