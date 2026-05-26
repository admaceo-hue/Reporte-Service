package com.mariluz.reportes.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariluz.reportes.service.ReportesService;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    private final ReportesService reportesService;

    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    // Ventas 

    @GetMapping("/ventas")
    public ResponseEntity<Map<String, Object>> getReporteVentas(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(reportesService.obtenerReporteVentas(authHeader));
    }

    //    productos más vendidos
  
    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<Map<String, Object>> getProductosMasVendidos(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(reportesService.obtenerProductosMasVendidos(authHeader));
    }
}