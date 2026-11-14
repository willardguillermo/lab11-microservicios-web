package com.tecsup.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Long idMedico;

    // Colegio Médico del Perú
    @Column(length = 20, unique = true)
    private String cmp;

    @Column(length = 100, nullable = false)
    private String nombres;

    @Column(length = 100, nullable = false)
    private String apellidos;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String correo;

    // Especialidad está en la misma BD (bd_medico), por eso sí usamos la relación JPA
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    @Column(length = 1)
    private String estado = "A";

    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }

    public String getCmp() { return cmp; }
    public void setCmp(String cmp) { this.cmp = cmp; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
