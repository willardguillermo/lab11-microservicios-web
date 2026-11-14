package com.tecsup.services;

import com.tecsup.client.MedicoClient;
import com.tecsup.client.PacienteClient;
import com.tecsup.dto.*;
import com.tecsup.exception.BadRequestException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Atencion;
import com.tecsup.model.Cita;
import com.tecsup.model.EstadoCita;
import com.tecsup.repository.AtencionRepository;
import com.tecsup.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaService {

    private static final List<String> DIAS =
            List.of("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO");

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private AtencionRepository atencionRepo;

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private MedicoClient medicoClient;

    public List<Cita> listar() {
        return citaRepo.findAll();
    }

    public Cita obtener(Long id) {
        return citaRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cita no encontrada con id: " + id));
    }

    public CitaDetalleDTO obtenerDetalle(Long id) {
        Cita c = obtener(id);
        return new CitaDetalleDTO(c, pacienteClient.obtener(c.getIdPaciente()), medicoClient.obtener(c.getIdMedico()));
    }

    public Cita registrar(CitaDTO dto) {
        PacienteResumen paciente = pacienteClient.obtener(dto.idPaciente());
        if (!"A".equals(paciente.estado())) {
            throw new BadRequestException("El paciente con id " + dto.idPaciente() + " está inactivo");
        }
        MedicoResumen medico = medicoClient.obtener(dto.idMedico());
        if (!"A".equals(medico.estado())) {
            throw new BadRequestException("El médico con id " + dto.idMedico() + " está inactivo");
        }
        validarHorario(dto.idMedico(), dto.fecha(), dto.hora());
        if (citaRepo.existsByIdMedicoAndFechaAndHoraAndEstadoNot(
                dto.idMedico(), dto.fecha(), dto.hora(), EstadoCita.CANCELADA)) {
            throw new BadRequestException("El médico ya tiene una cita el " + dto.fecha() + " a las " + dto.hora());
        }

        Cita c = new Cita();
        c.setFecha(dto.fecha());
        c.setHora(dto.hora());
        c.setIdPaciente(dto.idPaciente());
        c.setIdMedico(dto.idMedico());
        c.setMotivo(dto.motivo());
        return citaRepo.save(c);
    }

    // La hora debe caer dentro de un horario del médico para ese día de la semana
    private void validarHorario(Long idMedico, LocalDate fecha, LocalTime hora) {
        String dia = DIAS.get(fecha.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        boolean atiende = medicoClient.horarios(idMedico).stream()
                .anyMatch(h -> h.diaSemana().equals(dia)
                        && !hora.isBefore(h.horaInicio())
                        && hora.isBefore(h.horaFin()));
        if (!atiende) {
            throw new BadRequestException("El médico no atiende el " + dia + " a las " + hora);
        }
    }

    public Cita cambiarEstado(Long id, EstadoDTO dto) {
        EstadoCita nuevo = EstadoCita.valueOf(dto.estado());
        if (nuevo == EstadoCita.ATENDIDA) {
            throw new BadRequestException("Para marcar la cita como ATENDIDA registre la atención en POST /api/citas/" + id + "/atencion");
        }
        return aplicarEstado(obtener(id), nuevo);
    }

    public Cita cancelar(Long id) {
        return aplicarEstado(obtener(id), EstadoCita.CANCELADA);
    }

    @Transactional
    public Atencion registrarAtencion(Long id, AtencionDTO dto) {
        Cita cita = obtener(id);
        aplicarEstado(cita, EstadoCita.ATENDIDA);

        Atencion a = new Atencion();
        a.setCita(cita);
        a.setDiagnostico(dto.diagnostico());
        a.setTratamiento(dto.tratamiento());
        a.setObservaciones(dto.observaciones());
        return atencionRepo.save(a);
    }

    public Atencion obtenerAtencion(Long id) {
        obtener(id);
        return atencionRepo.findByCitaIdCita(id).orElseThrow(
                () -> new ResourceNotFoundException("La cita " + id + " aún no tiene atención registrada"));
    }

    private Cita aplicarEstado(Cita cita, EstadoCita nuevo) {
        if (!cita.getEstado().puedeCambiarA(nuevo)) {
            throw new BadRequestException("No se puede pasar una cita de " + cita.getEstado() + " a " + nuevo);
        }
        cita.setEstado(nuevo);
        return citaRepo.save(cita);
    }
}
