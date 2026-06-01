package com.chavez.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TallerDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cupo = 30;
    private Long instructorId;
    private Set<Long> alumnosIds;
    private Map<String, Object> instructor;
    private List<Map<String, Object>> alumnos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Integer getCupo() { return cupo; }
    public void setCupo(Integer cupo) { this.cupo = cupo; }
    public Long getInstructorId() { return instructorId; }
    public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }
    public Set<Long> getAlumnosIds() { return alumnosIds; }
    public void setAlumnosIds(Set<Long> alumnosIds) { this.alumnosIds = alumnosIds; }
    public Map<String, Object> getInstructor() { return instructor; }
    public void setInstructor(Map<String, Object> instructor) { this.instructor = instructor; }
    public List<Map<String, Object>> getAlumnos() { return alumnos; }
    public void setAlumnos(List<Map<String, Object>> alumnos) { this.alumnos = alumnos; }
}
