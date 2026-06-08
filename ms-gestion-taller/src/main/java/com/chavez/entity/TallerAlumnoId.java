package com.chavez.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TallerAlumnoId implements Serializable {

    @Column(name = "taller_id")
    private Long tallerId;

    @Column(name = "alumno_id")
    private Long alumnoId;

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
