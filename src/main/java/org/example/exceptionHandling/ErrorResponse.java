package org.example.exceptionHandling;


import java.time.LocalDateTime;


public record ErrorResponse(int errorCode, String message, String resourceId, LocalDateTime timestamp) {
}
