package com.tecsup.dto;

import jakarta.validation.constraints.*;

public record MedicoDTO(
        @NotBlank(message = "El CMP es obligatorio")
        @Size(max = 20, message = "El CMP no puede superar 20 caracteres")
        String cmp,

        @NotBlank(message = "Los nombres son obligatorios")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        String apellidos,

        @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
        String telefono,

        @Email(message = "El correo no es válido")
        String correo,

        @NotNull(message = "La especialidad es obligatoria")
        Long idEspecialidad,

        @Pattern(regexp = "[AI]", message = "El estado debe ser A (activo) o I (inactivo)")
        String estado) {
}
