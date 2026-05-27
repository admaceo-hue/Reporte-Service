package com.mariluz.reportes.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mariluz.reportes.dto.ErrorResponse;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

   
    @ExceptionHandler(MicroserviceConnectionException.class)
    public ResponseEntity<ErrorResponse> handleMicroserviceConnection(
        MicroserviceConnectionException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = Map.of(
            "error",
            "Error de comunicacion con otro microservicio"
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message(ex.getMessage())
                .errors(errors)
                .endpoint(request.getRequestURI())
                .build()
        );
    }

    // Handler usuario sin permisos para ver reportes
    @ExceptionHandler(UnauthorizedReportException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedReport(
        UnauthorizedReportException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = Map.of(
            "error",
            "Acceso denegado al reporte"
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .errors(errors)
                .endpoint(request.getRequestURI())
                .build()
        );
    }

    // Handler token JWT invalido o malformado
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(
        JwtException ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = Map.of(
            "error",
            "El token no es valido o ha expirado"
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Error de autenticacion")
                .errors(errors)
                .endpoint(request.getRequestURI())
                .build()
        );
    }

    // Handler generico para excepciones no contempladas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex,
        HttpServletRequest request
    ) {
        Map<String, String> errors = Map.of(
            "error",
            "Error inesperado en el servidor",
            "detail",
            ex.getMessage() != null ? ex.getMessage() : "sin detalles"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor")
                .errors(errors)
                .endpoint(request.getRequestURI())
                .build()
        );
    }
}