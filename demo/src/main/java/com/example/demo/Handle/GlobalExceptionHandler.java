package com.example.demo.Handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Lidar com erros de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return buildErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    // Lidar com erros de recursos não encontrados
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildErrorResponse("Recurso não encontrado: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Lidar com erros genéricos (RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Lidar com erros genéricos de qualquer outra exceção
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        return buildErrorResponse("Erro inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método auxiliar para construir respostas de erro
    private ResponseEntity<Map<String, String>> buildErrorResponse(Object message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        if (message instanceof String) {
            error.put("error", (String) message);
        } else if (message instanceof Map) {
            error.putAll((Map<String, String>) message);
        }
        return new ResponseEntity<>(error, status);
    }
}
