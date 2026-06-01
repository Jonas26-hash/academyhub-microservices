package com.chavez.repository;

import com.chavez.entity.TallerAlumno;
import com.chavez.entity.TallerAlumnoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TallerAlumnoRepository extends JpaRepository<TallerAlumno, TallerAlumnoId> {

    @Query("SELECT ta FROM TallerAlumno ta WHERE ta.taller.id = :tallerId")
    List<TallerAlumno> findByTallerId(@Param("tallerId") Long tallerId);

    @Modifying
    @Query("DELETE FROM TallerAlumno ta WHERE ta.taller.id = :tallerId AND ta.id.alumnoId = :alumnoId")
    void deleteByTallerYAlumno(@Param("tallerId") Long tallerId, @Param("alumnoId") Long alumnoId);

    @Query("SELECT COUNT(ta) > 0 FROM TallerAlumno ta WHERE ta.taller.id = :tallerId AND ta.id.alumnoId = :alumnoId")
    boolean existsByTallerYAlumno(@Param("tallerId") Long tallerId, @Param("alumnoId") Long alumnoId);
}
