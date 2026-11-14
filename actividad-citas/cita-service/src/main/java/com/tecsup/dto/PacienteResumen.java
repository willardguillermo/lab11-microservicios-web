package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Lo que nos interesa de la respuesta de paciente-service
@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteResumen(Long idPaciente, String dni, String nombres, String apellidos, String estado) {
}
