package com.tecsup.client;

import com.tecsup.dto.HorarioResumen;
import com.tecsup.dto.MedicoResumen;
import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ServiceCommunicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

// Comunicación HTTP con medico-service
@Component
public class MedicoClient {

    private final RestClient restClient;

    public MedicoClient(RestClient.Builder builder, @Value("${servicios.medico.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public MedicoResumen obtener(Long id) {
        try {
            return restClient.get().uri("/api/medicos/{id}", id).retrieve().body(MedicoResumen.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException("El médico con id " + id + " no existe en medico-service");
        } catch (RestClientException ex) {
            throw new ServiceCommunicationException("No se pudo comunicar con medico-service");
        }
    }

    public List<HorarioResumen> horarios(Long idMedico) {
        try {
            return restClient.get().uri("/api/medicos/{id}/horarios", idMedico).retrieve()
                    .body(new ParameterizedTypeReference<List<HorarioResumen>>() {});
        } catch (RestClientException ex) {
            throw new ServiceCommunicationException("No se pudo comunicar con medico-service");
        }
    }
}
