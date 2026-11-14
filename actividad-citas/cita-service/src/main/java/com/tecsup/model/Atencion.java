package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

// Resultado de la cita (diagnóstico y tratamiento)
@Entity
@Table(name = "atencion")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atencion")
    private Long idAtencion;

    @JsonIgnore
    @OneToOne(optional = false)
    @JoinColumn(name = "id_cita", unique = true, nullable = false)
    private Cita cita;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String tratamiento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    void alRegistrar() {
        fechaRegistro = LocalDateTime.now();
    }

    @JsonProperty("idCita")
    public Long getIdCita() { return cita.getIdCita(); }

    public Long getIdAtencion() { return idAtencion; }
    public void setIdAtencion(Long idAtencion) { this.idAtencion = idAtencion; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
