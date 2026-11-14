package com.tecsup.client;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ServiceCommunicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

// Comunicación HTTP con categoria-service
@Component
public class CategoriaClient {

    private final RestClient restClient;

    public CategoriaClient(RestClient.Builder builder,
                           @Value("${servicios.categoria.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public CategoriaDTO obtener(Long id) {
        try {
            return restClient.get()
                    .uri("/api/categorias/{id}", id)
                    .retrieve()
                    .body(CategoriaDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException("La categoría con id " + id + " no existe en categoria-service");
        } catch (RestClientException ex) {
            throw new ServiceCommunicationException("No se pudo comunicar con categoria-service");
        }
    }
}
