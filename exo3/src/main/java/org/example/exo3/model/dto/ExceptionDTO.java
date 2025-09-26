package org.example.exo3.model.dto;

import java.time.LocalDateTime;

public record ExceptionDTO(String message, int status, LocalDateTime time) {
}