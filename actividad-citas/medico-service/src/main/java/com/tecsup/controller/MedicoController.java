package com.tecsup.controller;

import com.tecsup.dto.HorarioDTO;
import com.tecsup.dto.MedicoDTO;
import com.tecsup.model.HorarioMedico;
import com.tecsup.model.Medico;
import com.tecsup.services.MedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService service;

    @GetMapping
    public ResponseEntity<List<Medico>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Medico> registrar(@Valid @RequestBody MedicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> actualizar(@PathVariable Long id, @Valid @RequestBody MedicoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Médico eliminado correctamente"));
    }

    @GetMapping("/{id}/horarios")
    public ResponseEntity<List<HorarioMedico>> listarHorarios(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarHorarios(id));
    }

    @PostMapping("/{id}/horarios")
    public ResponseEntity<HorarioMedico> agregarHorario(@PathVariable Long id, @Valid @RequestBody HorarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agregarHorario(id, dto));
    }
}
