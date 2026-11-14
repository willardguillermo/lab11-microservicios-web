package com.tecsup.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaDTO(
        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha no puede ser pasada")
        LocalDate fecha,

        @NotNull(message = "La hora es obligatoria")
        LocalTime hora,

        @NotNull(message = "El paciente es obligatorio")
        Long idPaciente,

        @NotNull(message = "El médico es obligatorio")
        Long idMedico,

        @Size(max = 300, message = "El motivo no puede superar 300 caracteres")
        String motivo) {
}
