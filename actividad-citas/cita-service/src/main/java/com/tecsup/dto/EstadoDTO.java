package com.tecsup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EstadoDTO(
        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "PROGRAMADA|CONFIRMADA|ATENDIDA|CANCELADA",
                 message = "El estado debe ser PROGRAMADA, CONFIRMADA, ATENDIDA o CANCELADA")
        String estado) {
}
