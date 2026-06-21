package com.mariluz.reportes.client;

import com.mariluz.reportes.dto.MyReservationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class AgendaClient {

    private final RestClient restClient;

    public AgendaClient(@Value("${agenda.service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public List<MyReservationResponse> obtenerTodasLasReservas(String authHeader) {
        try {
            return restClient
                    .get()
                    .uri("/reservas") 
                    .header("Authorization", authHeader)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<MyReservationResponse>>() {});
     } catch (RestClientException e) {
            throw new com.mariluz.reportes.exception.MicroserviceConnectionException("No se pudo obtener el listado de reservas en Reportes.");
        }
    }
}