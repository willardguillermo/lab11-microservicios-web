package com.tecsup.client;

import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ServiceCommunicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

// Comunicación HTTP con producto-service
@Component
public class ProductoClient {

    private final RestClient restClient;

    public ProductoClient(RestClient.Builder builder, @Value("${servicios.producto.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public void verificarExiste(Long idProducto) {
        try {
            restClient.get().uri("/api/productos/{id}", idProducto).retrieve().toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException("El producto con id " + idProducto + " no existe en producto-service");
        } catch (RestClientException ex) {
            throw new ServiceCommunicationException("No se pudo comunicar con producto-service");
        }
    }
}
