package com.chavez.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "saga_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SagaLog {

    public enum Tipo { INSCRIPCION_ALUMNO }
    public enum Estado { PENDING, COMPLETED, COMPENSATED, FAILED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Long tallerId;
    private Long alumnoId;
    private LocalDateTime creadoEn;
    private LocalDateTime completadoEn;

    private String errorMensaje;

    public SagaLog(Tipo tipo, Long tallerId, Long alumnoId) {
        this.tipo = tipo;
        this.estado = Estado.PENDING;
        this.tallerId = tallerId;
        this.alumnoId = alumnoId;
        this.creadoEn = LocalDateTime.now();
    }
}
