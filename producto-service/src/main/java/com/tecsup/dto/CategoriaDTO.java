package com.tecsup.dto;

// Datos que devuelve categoria-service (GET /api/categorias/{id})
public record CategoriaDTO(Long id, String nombre, String estado) {
}
