package com.chavez.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "taller_alumno")
public class TallerAlumno {

    @EmbeddedId
    private TallerAlumnoId id;

    @MapsId("tallerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taller_id")
    private Taller taller;

    public TallerAlumno() {}

    public TallerAlumno(Taller taller, Long alumnoId) {
        this.taller = taller;
        this.id = new TallerAlumnoId(taller.getId(), alumnoId);
    }

    public TallerAlumnoId getId() { return id; }
    public void setId(TallerAlumnoId id) { this.id = id; }
    public Taller getTaller() { return taller; }
    public void setTaller(Taller taller) { this.taller = taller; }
}
