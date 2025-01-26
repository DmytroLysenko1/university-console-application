package org.example.exceptionHandling.customExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private final int errorCode;
    private final String resourceId;
    private final LocalDateTime timestamp;

    public NotFoundException(String message, String resourceId) {
        super(message);
        this.errorCode = 404;
        this.resourceId = resourceId;
        this.timestamp = LocalDateTime.now();
    }
}
