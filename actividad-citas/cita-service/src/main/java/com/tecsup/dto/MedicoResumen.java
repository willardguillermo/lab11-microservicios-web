package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Lo que nos interesa de la respuesta de medico-service
@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoResumen(Long idMedico, String cmp, String nombres, String apellidos,
                            Especialidad especialidad, String estado) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Especialidad(String nombre) {
    }
}
