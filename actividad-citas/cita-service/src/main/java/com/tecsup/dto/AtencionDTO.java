package com.tecsup.dto;

import jakarta.validation.constraints.NotBlank;

public record AtencionDTO(
        @NotBlank(message = "El diagnóstico es obligatorio")
        String diagnostico,

        String tratamiento,

        String observaciones) {
}
