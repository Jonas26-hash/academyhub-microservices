package com.chavez.service;

import com.chavez.dto.AlumnoDTO;
import java.util.List;

public interface AlumnoService {
    List<AlumnoDTO> listarTodos();
    AlumnoDTO obtenerPorId(Long id);
    AlumnoDTO crear(AlumnoDTO dto);
    AlumnoDTO actualizar(Long id, AlumnoDTO dto);
    void eliminar(Long id);
}
