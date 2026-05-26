package com.mariluz.reportes.client;

import com.mariluz.reportes.dto.SaleResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class SalesClient {

    private final RestClient restClient;

    public SalesClient(@Value("${sales.service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public List<SaleResponse> obtenerTodasLasVentas(String authHeader) {
        try {
            return restClient
                    .get()
                    .uri("/todas") 
                    .header("Authorization", authHeader)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<SaleResponse>>() {});
     } catch (RestClientException e) {
            throw new com.mariluz.reportes.exception.MicroserviceConnectionException("No se pudo obtener el listado de ventas en Reportes.");
        }
    }
}