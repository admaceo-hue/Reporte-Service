package com.mariluz.reportes.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.mariluz.reportes.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Reportes",
    description = "Reportes y estadisticas para administradores (requiere token JWT con rol ADMIN)"
)
public interface ReportesApi {

    // 1. reporte de ventas
    @Operation(
        summary = "Reporte de ventas",
        description = "Devuelve el total de ganancias y el numero de ventas totales. Solo accesible para administradores."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Reporte de ventas generado correctamente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o expirado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/ventas",
                        "errors": { "error": "Token JWT inválido o expirado" },
                        "message": "No autenticado",
                        "status": 401,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "El usuario autenticado no tiene permisos de administrador.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/ventas",
                        "errors": { "error": "Solo un administrador puede acceder a los reportes" },
                        "message": "Acceso denegado",
                        "status": 403,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "503",
            description = "No se pudo conectar con un microservicio dependiente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/ventas",
                        "errors": { "error": "No se pudo conectar con el servicio de ventas" },
                        "message": "Servicio no disponible",
                        "status": 503,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/ventas",
                        "errors": { "error": "Error inesperado" },
                        "message": "Error interno del servidor",
                        "status": 500,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getReporteVentas(String authHeader);

    // 2. reporte de productos mas vendidos
    @Operation(
        summary = "Productos más vendidos",
        description = "Devuelve el listado de productos ordenados por unidades vendidas. Solo accesible para administradores."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Reporte de productos más vendidos generado correctamente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido o expirado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/productos-mas-vendidos",
                        "errors": { "error": "Token JWT inválido o expirado" },
                        "message": "No autenticado",
                        "status": 401,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "El usuario autenticado no tiene permisos de administrador.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/productos-mas-vendidos",
                        "errors": { "error": "Solo un administrador puede acceder a los reportes" },
                        "message": "Acceso denegado",
                        "status": 403,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "503",
            description = "No se pudo conectar con un microservicio dependiente.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/productos-mas-vendidos",
                        "errors": { "error": "No se pudo conectar con el servicio de ventas" },
                        "message": "Servicio no disponible",
                        "status": 503,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "endpoint": "/reportes/productos-mas-vendidos",
                        "errors": { "error": "Error inesperado" },
                        "message": "Error interno del servidor",
                        "status": 500,
                        "timeStamp": "2026-06-20T05:11:58"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getProductosMasVendidos(String authHeader);
}
