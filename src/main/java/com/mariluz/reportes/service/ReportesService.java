package com.mariluz.reportes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mariluz.reportes.client.CatalogClient;
import com.mariluz.reportes.client.SalesClient;
import com.mariluz.reportes.dto.GetProductsResponse;
import com.mariluz.reportes.dto.ProductResponse;
import com.mariluz.reportes.dto.SaleResponse;
import com.mariluz.reportes.exception.UnauthorizedReportException;
import com.mariluz.reportes.model.User;

@Service
public class ReportesService {

    private final SalesClient salesClient;
    private final CatalogClient catalogClient;

    public ReportesService(SalesClient salesClient, CatalogClient catalogClient) {
        this.salesClient = salesClient;
        this.catalogClient = catalogClient;
    }

    // Obtiene el usuario autenticado desde el contexto de seguridad
  
    private User getCurrentUser() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthorizedReportException("No hay un usuario autenticado");
        }
        return user;
    }

    
    private void validarAdmin() {
        User user = getCurrentUser();
        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new UnauthorizedReportException(
                "Solo un administrador puede acceder a los reportes");
        }
    }

    // Reporte de ventas generales (dinero)
    public Map<String, Object> obtenerReporteVentas(String authHeader) {
        validarAdmin();

        List<SaleResponse> ventas = salesClient.obtenerTodasLasVentas(authHeader);

        int totalGanancias = 0;
        int ventasTotales = 0;

        if (ventas != null) {
            ventasTotales = ventas.size();
            for (SaleResponse venta : ventas) {
                // Solo sumamos las ventas completadas 
                if (venta.getTotal() != null
                        && "COMPLETED".equalsIgnoreCase(venta.getStatus())) {
                    totalGanancias += venta.getTotal();
                }
            }
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("totalGanancias", totalGanancias);
        respuesta.put("ventasTotales", ventasTotales);
        return respuesta;
    }

    // Reporte de productos mas vendidos
    public Map<String, Object> obtenerProductosMasVendidos(String authHeader) {
        validarAdmin();

        List<SaleResponse> ventas = salesClient.obtenerTodasLasVentas(authHeader);
        Map<Integer, Integer> conteoProductos = new HashMap<>(); 

        // Contamos cuantas unidades se vendieron de cada producto
        if (ventas != null) {
            for (SaleResponse venta : ventas) {
                if (venta.getProducts() != null) {
                    for (var item : venta.getProducts()) {
                        conteoProductos.put(item.getProductId(),
                            conteoProductos.getOrDefault(item.getProductId(), 0)
                                + item.getQuantity());
                    }
                }
            }
        }

        List<Map<String, Object>> topProductos = new ArrayList<>();

        if (!conteoProductos.isEmpty()) {

            // Ordenamos los ids de mayor a menor 
            List<Integer> idsOrdenados = conteoProductos.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            try {
                // Pedimos al catalogo los nombres reales de esos productos
                GetProductsResponse catalogoResp =
                    catalogClient.findProducts(idsOrdenados, authHeader);

                // Guardamos los productos del catalogo en un mapa para buscarlos rapido
                Map<Integer, ProductResponse> catalogoMap = new HashMap<>();
                if (catalogoResp != null && catalogoResp.getProducts() != null) {
                    for (ProductResponse prod : catalogoResp.getProducts()) {
                        catalogoMap.put(prod.getId(), prod);
                    }
                }

                
                for (Integer id : idsOrdenados) {
                    Map<String, Object> prodMap = new HashMap<>();
                    ProductResponse prod = catalogoMap.get(id);
                    prodMap.put("productId", String.valueOf(id));
                    prodMap.put("name", prod != null ? prod.getName()
                            : "Producto ID: " + id + " (no encontrado)");
                    prodMap.put("unitsSold", conteoProductos.get(id));
                    topProductos.add(prodMap);
                }

            } catch (Exception e) {
                
                for (Integer id : idsOrdenados) {
                    Map<String, Object> prodMap = new HashMap<>();
                    prodMap.put("productId", String.valueOf(id));
                    prodMap.put("name", "Producto ID: " + id + " (Catalogo inaccesible)");
                    prodMap.put("unitsSold", conteoProductos.get(id));
                    topProductos.add(prodMap);
                }
            }
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("topProductos", topProductos);
        return respuesta;
    }
}