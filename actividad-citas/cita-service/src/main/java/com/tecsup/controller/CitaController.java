package com.tecsup.controller;

import com.tecsup.dto.AtencionDTO;
import com.tecsup.dto.CitaDTO;
import com.tecsup.dto.CitaDetalleDTO;
import com.tecsup.dto.EstadoDTO;
import com.tecsup.model.Atencion;
import com.tecsup.model.Cita;
import com.tecsup.services.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService service;

    @GetMapping
    public ResponseEntity<List<Cita>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<CitaDetalleDTO> obtenerDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerDetalle(id));
    }

    @PostMapping
    public ResponseEntity<Cita> registrar(@Valid @RequestBody CitaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoDTO dto) {
        return ResponseEntity.ok(service.cambiarEstado(id, dto));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @PostMapping("/{id}/atencion")
    public ResponseEntity<Atencion> registrarAtencion(@PathVariable Long id, @Valid @RequestBody AtencionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarAtencion(id, dto));
    }

    @GetMapping("/{id}/atencion")
    public ResponseEntity<Atencion> obtenerAtencion(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerAtencion(id));
    }
}
