package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HorarioResumen(String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
}
