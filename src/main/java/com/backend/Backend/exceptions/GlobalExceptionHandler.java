package com.backend.Backend.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(LocalDateTime.class)) {
            return ResponseEntity.badRequest()
                    .body("Formato de fecha inválido. Usar ISO 8601: yyyy-MM-ddTHH:mm:ss");
        }
        return ResponseEntity.badRequest()
                .body("Parámetro inválido: " + ex.getName());
    }
}
