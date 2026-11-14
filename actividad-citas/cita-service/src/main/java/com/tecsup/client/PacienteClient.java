package com.tecsup.client;

import com.tecsup.dto.PacienteResumen;
import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ServiceCommunicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

// Comunicación HTTP con paciente-service
@Component
public class PacienteClient {

    private final RestClient restClient;

    public PacienteClient(RestClient.Builder builder, @Value("${servicios.paciente.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public PacienteResumen obtener(Long id) {
        try {
            return restClient.get().uri("/api/pacientes/{id}", id).retrieve().body(PacienteResumen.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException("El paciente con id " + id + " no existe en paciente-service");
        } catch (RestClientException ex) {
            throw new ServiceCommunicationException("No se pudo comunicar con paciente-service");
        }
    }
}
