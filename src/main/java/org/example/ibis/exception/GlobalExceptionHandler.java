package org.example.ibis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleSheetsIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Error de conexión con Google Sheets",
                        "message", "No se pudo acceder a la hoja de cálculo",
                        "detail", ex.getMessage()
                ));
    }

    @ExceptionHandler(GeneralSecurityException.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(GeneralSecurityException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Error de configuración",
                        "message", "Problema con las credenciales de Google Sheets",
                        "detail", ex.getMessage()
                ));
    }
}