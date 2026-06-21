package com.mariluz.reportes.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mariluz.reportes.exception.MicroserviceConnectionException;
import com.mariluz.reportes.exception.UnauthorizedReportException;
import com.mariluz.reportes.security.JwtUtil;
import com.mariluz.reportes.service.ReportesService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReportesController.class)
@AutoConfigureMockMvc(addFilters = false) // desactiva filtro JWT y seguridad para ejecutar el test
public class ReportesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportesService service;

    @MockitoBean
    private JwtUtil jwtUtil; // importante para que funcione el contexto de seguridad

    // -------------- 1. REPORTE DE VENTAS --------------
    // 200
    @Test
    public void testReporteVentas() throws Exception {
        when(service.obtenerReporteVentas("Bearer test")).thenReturn(
            Map.of("total", 100)
        );

        mockMvc
            .perform(
                get("/reportes/ventas").header("Authorization", "Bearer test")
            )
            .andExpect(status().isOk());
    }

    // 403
    @Test
    public void testReporteVentasForbidden() throws Exception {
        when(service.obtenerReporteVentas("Bearer test")).thenThrow(
            new UnauthorizedReportException(
                "Solo un administrador puede acceder a los reportes"
            )
        );

        mockMvc
            .perform(
                get("/reportes/ventas").header("Authorization", "Bearer test")
            )
            .andExpect(status().isForbidden());
    }

    // -------------- 2. PRODUCTOS MAS VENDIDOS --------------
    // 200
    @Test
    public void testProductosMasVendidos() throws Exception {
        when(service.obtenerProductosMasVendidos("Bearer test")).thenReturn(
            Map.of("producto", "x")
        );

        mockMvc
            .perform(
                get("/reportes/productos-mas-vendidos").header(
                    "Authorization",
                    "Bearer test"
                )
            )
            .andExpect(status().isOk());
    }

    // 503
    @Test
    public void testProductosServiceUnavailable() throws Exception {
        when(service.obtenerProductosMasVendidos("Bearer test")).thenThrow(
            new MicroserviceConnectionException(
                "No se pudo conectar con el servicio de ventas"
            )
        );

        mockMvc
            .perform(
                get("/reportes/productos-mas-vendidos").header(
                    "Authorization",
                    "Bearer test"
                )
            )
            .andExpect(status().isServiceUnavailable());
    }
}
