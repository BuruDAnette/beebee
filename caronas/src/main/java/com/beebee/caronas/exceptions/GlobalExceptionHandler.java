// GlobalExceptionHandler.java
package com.beebee.caronas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.stream.Collectors;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("• %s: %s", 
                capitalize(error.getField()), 
                error.getDefaultMessage()))
            .collect(Collectors.joining("\n"));
        
        return ResponseEntity.badRequest()
            .body("Por favor, corrija os seguintes erros:\n" + errorMessage);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest()
            .body(String.format("Erro de validação:\n• %s: %s", 
                capitalize(ex.getField()), 
                ex.getError()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> handleBusinessRule(BusinessRuleException ex) {
        return ResponseEntity.badRequest()
            .body("Regra de negócio violada:\n• " + ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Recurso não encontrado:\n• " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erro interno no servidor:\n• " + 
                (ex.getMessage() != null ? ex.getMessage() : "Ocorreu um erro inesperado"));
    }

    private String capitalize(String field) {
        if (field == null || field.isEmpty()) {
            return field;
        }
        String readableName = field.replace("_", " ");
        return readableName.substring(0, 1).toUpperCase() + readableName.substring(1);
    }
}