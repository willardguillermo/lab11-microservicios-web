package com.tecsup.dto;

import com.tecsup.model.Producto;

// Producto + su categoría obtenida de categoria-service
public record ProductoDetalleDTO(Long id, String nombre, Double precio, Integer stock, CategoriaDTO categoria) {

    public ProductoDetalleDTO(Producto p, CategoriaDTO categoria) {
        this(p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), categoria);
    }
}
