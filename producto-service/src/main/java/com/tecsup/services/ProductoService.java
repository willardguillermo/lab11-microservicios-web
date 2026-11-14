package com.tecsup.services;

import com.tecsup.client.CategoriaClient;
import com.tecsup.dto.ProductoDTO;
import com.tecsup.dto.ProductoDetalleDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    @Autowired
    private CategoriaClient categoriaClient;

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    // Combina el producto con los datos de su categoría (llamada HTTP)
    public ProductoDetalleDTO obtenerDetalle(Long id) {
        Producto p = obtener(id);
        return new ProductoDetalleDTO(p, categoriaClient.obtener(p.getCategoriaId()));
    }

    public Producto registrar(ProductoDTO dto) {
        Producto p = new Producto();
        copiar(dto, p);
        return repo.save(p);
    }

    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto p = obtener(id);
        copiar(dto, p);
        return repo.save(p);
    }

    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }

    private void copiar(ProductoDTO dto, Producto p) {
        // Antes de guardar, verifica en categoria-service que la categoría exista
        categoriaClient.obtener(dto.getCategoriaId());
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        p.setCategoriaId(dto.getCategoriaId());
    }
}
