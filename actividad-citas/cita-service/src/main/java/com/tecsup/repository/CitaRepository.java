package com.tecsup.repository;

import com.tecsup.model.Cita;
import com.tecsup.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // ¿El médico ya tiene una cita activa en esa fecha y hora?
    boolean existsByIdMedicoAndFechaAndHoraAndEstadoNot(Long idMedico, LocalDate fecha, LocalTime hora, EstadoCita estado);
}
