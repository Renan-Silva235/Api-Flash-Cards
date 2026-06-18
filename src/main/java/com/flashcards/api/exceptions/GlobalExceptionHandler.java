package com.flashcards.api.exceptions;

import com.flashcards.api.dtos.response.InstantExceptionResponseDTO;
import com.flashcards.api.dtos.response.ValidationErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDTO>> handleValidationError(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();

        List<ValidationErrorDTO> response = errors.stream()
                .map(ValidationErrorDTO::new)
                .toList();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<InstantExceptionResponseDTO> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        InstantExceptionResponseDTO error = new InstantExceptionResponseDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Não autorizado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
