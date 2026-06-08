package com.chavez.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TallerDTO {
    private Long id;

    @NotBlank(message = "El nombre del taller es obligatorio")
    @Size(max = 150, message = "El nombre no debe exceder 150 caracteres")
    private String nombre;

    private String descripcion;

    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o futura")
    private LocalDate fechaInicio;

    @FutureOrPresent(message = "La fecha de fin debe ser hoy o futura")
    private LocalDate fechaFin;

    @Min(value = 1, message = "El cupo mínimo es 1")
    @Max(value = 999, message = "El cupo máximo es 999")
    private Integer cupo = 30;

    @NotNull(message = "El instructorId es obligatorio")
    private Long instructorId;

    private Set<Long> alumnosIds;
    private Map<String, Object> instructor;
    private List<Map<String, Object>> alumnos;
}
