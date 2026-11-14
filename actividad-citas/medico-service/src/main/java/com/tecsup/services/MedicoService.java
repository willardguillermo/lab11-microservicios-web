package com.tecsup.services;

import com.tecsup.dto.HorarioDTO;
import com.tecsup.dto.MedicoDTO;
import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Especialidad;
import com.tecsup.model.HorarioMedico;
import com.tecsup.model.Medico;
import com.tecsup.repository.EspecialidadRepository;
import com.tecsup.repository.HorarioMedicoRepository;
import com.tecsup.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository repo;

    @Autowired
    private EspecialidadRepository especialidadRepo;

    @Autowired
    private HorarioMedicoRepository horarioRepo;

    public List<Medico> listar() {
        return repo.findAll();
    }

    public Medico obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Médico no encontrado con id: " + id));
    }

    public Medico registrar(MedicoDTO dto) {
        Medico m = new Medico();
        copiar(dto, m);
        return repo.save(m);
    }

    public Medico actualizar(Long id, MedicoDTO dto) {
        Medico m = obtener(id);
        copiar(dto, m);
        return repo.save(m);
    }

    // Borra primero sus horarios para no romper la FK de horario_medico
    @Transactional
    public void eliminar(Long id) {
        Medico m = obtener(id);
        horarioRepo.deleteByMedicoIdMedico(id);
        repo.delete(m);
    }

    public List<HorarioMedico> listarHorarios(Long idMedico) {
        obtener(idMedico);
        return horarioRepo.findByMedicoIdMedico(idMedico);
    }

    public HorarioMedico agregarHorario(Long idMedico, HorarioDTO dto) {
        if (!dto.horaFin().isAfter(dto.horaInicio())) {
            throw new BadRequestException("La hora de fin debe ser posterior a la hora de inicio");
        }
        HorarioMedico h = new HorarioMedico();
        h.setMedico(obtener(idMedico));
        h.setDiaSemana(dto.diaSemana());
        h.setHoraInicio(dto.horaInicio());
        h.setHoraFin(dto.horaFin());
        return horarioRepo.save(h);
    }

    private void copiar(MedicoDTO dto, Medico m) {
        Especialidad esp = especialidadRepo.findById(dto.idEspecialidad()).orElseThrow(
                () -> new BadRequestException("La especialidad con id " + dto.idEspecialidad() + " no existe"));
        m.setCmp(dto.cmp());
        m.setNombres(dto.nombres());
        m.setApellidos(dto.apellidos());
        m.setTelefono(dto.telefono());
        m.setCorreo(dto.correo());
        m.setEspecialidad(esp);
        if (dto.estado() != null) {
            m.setEstado(dto.estado());
        }
    }
}
