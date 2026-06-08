package com.chavez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taller_alumno")
@Getter @Setter @NoArgsConstructor
public class TallerAlumno {

    @EmbeddedId
    private TallerAlumnoId id;

    @MapsId("tallerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taller_id")
    private Taller taller;

    public TallerAlumno(Taller taller, Long alumnoId) {
        this.taller = taller;
        this.id = new TallerAlumnoId(taller.getId(), alumnoId);
    }
}
