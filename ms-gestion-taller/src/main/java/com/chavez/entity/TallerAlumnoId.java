package com.chavez.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TallerAlumnoId implements Serializable {

    @Column(name = "taller_id")
    private Long tallerId;

    @Column(name = "alumno_id")
    private Long alumnoId;

    public TallerAlumnoId() {}

    public TallerAlumnoId(Long tallerId, Long alumnoId) {
        this.tallerId = tallerId;
        this.alumnoId = alumnoId;
    }

    public Long getTallerId() { return tallerId; }
    public void setTallerId(Long tallerId) { this.tallerId = tallerId; }
    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TallerAlumnoId that)) return false;
        return Objects.equals(tallerId, that.tallerId) && Objects.equals(alumnoId, that.alumnoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tallerId, alumnoId);
    }
}
