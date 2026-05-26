package com.mariluz.reportes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mariluz.reportes.client.CatalogClient;
import com.mariluz.reportes.client.SalesClient;
import com.mariluz.reportes.dto.GetProductsResponse;
import com.mariluz.reportes.dto.SaleResponse;

@Service
public class ReportesService {

    private final SalesClient salesClient;
    private final CatalogClient catalogClient;

    public ReportesService(SalesClient salesClient, CatalogClient catalogClient) {
        this.salesClient = salesClient;
        this.catalogClient = catalogClient;
    }

    // Ventas generales (Dinero )
    public Map<String, Object> obtenerReporteVentas(String authHeader) {
        List<SaleResponse> ventas = salesClient.obtenerTodasLasVentas(authHeader);
        
        int totalGanancias = 0;
        int ventasTotales = 0;

        if (ventas != null) {
            ventasTotales = ventas.size();
            for (SaleResponse venta : ventas) {
                // Sumamos solo si la venta está completada o tiene un total válido
                if (venta.getTotal() != null) {
                    totalGanancias += venta.getTotal();
                }
            }
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("totalGanancias", totalGanancias);
        respuesta.put("ventasTotales", ventasTotales);
        return respuesta;
    }

   // Productos más vendidos
    public Map<String, Object> obtenerProductosMasVendidos(String authHeader) {
        List<SaleResponse> ventas = salesClient.obtenerTodasLasVentas(authHeader);
        Map<Integer, Integer> conteoProductos = new HashMap<>(); // idProducto -> unidadesVendidas

        //  Contar cuántas unidades se vendieron de ese producto
        if (ventas != null) {
            for (SaleResponse venta : ventas) {
                if (venta.getProducts() != null) {
                    for (var item : venta.getProducts()) {
                        conteoProductos.put(item.getProductId(), 
                                conteoProductos.getOrDefault(item.getProductId(), 0) + item.getQuantity());
                    }
                }
            }
        }

        List<Map<String, Object>> topProductos = new ArrayList<>();

        //   ordenamos de mayor a menor y buscamos sus nombres en el Catálogo
        if (!conteoProductos.isEmpty()) {
            
            List<Integer> idsOrdenados = conteoProductos.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            try {
                // Le pedimos al catálogo la info de estos IDs para tener los nombres reales
                GetProductsResponse catalogoResp = catalogClient.findProducts(idsOrdenados, authHeader);
                
                if (catalogoResp != null && catalogoResp.getProducts() != null) {
                    for (var prod : catalogoResp.getProducts()) {
                        Map<String, Object> prodMap = new HashMap<>();
                        prodMap.put("productId", String.valueOf(prod.getId()));
                        prodMap.put("name", prod.getName());
                        prodMap.put("unitsSold", conteoProductos.get(prod.getId()));
                        topProductos.add(prodMap);
                    }
                }
            } catch (Exception e) {
                // Si el catálogo falla, al menos mostramos los IDs y las cantidades
                for (Integer id : idsOrdenados) {
                    Map<String, Object> prodMap = new HashMap<>();
                    prodMap.put("productId", String.valueOf(id));
                    prodMap.put("name", "Producto ID: " + id + " (Catálogo inaccesible)");
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