package com.tecsup.client;

import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ServiceCommunicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

// Comunicación HTTP con cliente-service
@Component
public class ClienteClient {

    private final RestClient restClient;

    public ClienteClient(RestClient.Builder builder, @Value("${servicios.cliente.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public void verificarExiste(Long idCliente) {
        try {
            restClient.get().uri("/api/clientes/{id}", idCliente).retrieve().toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException("El cliente con id " + idCliente + " no existe en cliente-service");
        } catch (RestClientException ex) {
            throw new ServiceCommunicationException("No se pudo comunicar con cliente-service");
        }
    }
}
