package com.chavez.service;

import com.chavez.dto.TallerDTO;
import java.util.List;
import java.util.Map;

public interface TallerService {
    List<TallerDTO> listarTodos();
    TallerDTO obtenerPorId(Long id);
    TallerDTO crear(TallerDTO dto);
    TallerDTO actualizar(Long id, TallerDTO dto);
    void eliminar(Long id);
    void inscribirAlumno(Long tallerId, Long alumnoId);
    void desinscribirAlumno(Long tallerId, Long alumnoId);
    Map<String, Object> obtenerInstructorDelTaller(Long tallerId);
    List<Map<String, Object>> obtenerAlumnosDelTaller(Long tallerId);
}
