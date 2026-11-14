package com.tecsup.dto;

import com.tecsup.model.Cita;

// Cita con los datos del paciente y del médico obtenidos de sus microservicios
public record CitaDetalleDTO(Cita cita, PacienteResumen paciente, MedicoResumen medico) {
}
