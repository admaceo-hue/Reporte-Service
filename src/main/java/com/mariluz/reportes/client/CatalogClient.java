package com.mariluz.reportes.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.mariluz.reportes.dto.CatalogRequest;
import com.mariluz.reportes.dto.GetProductsResponse;

@Service
public class CatalogClient {

    private final RestClient restClient;

    public CatalogClient(@Value("${catalog.service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public GetProductsResponse findProducts(List<Integer> ids, String authHeader) {
        CatalogRequest content = CatalogRequest.builder().ids(ids).build();
        try {
            return restClient
                    .post()
                    .uri("/catalog/products/ids")
                    .header("Authorization", authHeader)
                    .body(content)
                    .retrieve()
                    .body(GetProductsResponse.class);
   } catch (RestClientException e) {
            throw new com.mariluz.reportes.exception.MicroserviceConnectionException("No se pudo obtener los productos del catalogo en Reportes.");
        }
    }
}