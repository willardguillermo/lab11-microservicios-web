package com.tecsup.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalTime;

public record HorarioDTO(
        @NotNull(message = "El día es obligatorio")
        @Pattern(regexp = "LUNES|MARTES|MIERCOLES|JUEVES|VIERNES|SABADO|DOMINGO",
                 message = "El día debe ser LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO o DOMINGO")
        String diaSemana,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime horaInicio,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime horaFin) {
}
