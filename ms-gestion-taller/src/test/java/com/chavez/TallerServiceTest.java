package com.chavez;

import com.chavez.dto.TallerDTO;
import com.chavez.entity.Taller;
import com.chavez.exception.ResourceNotFoundException;
import com.chavez.repository.TallerAlumnoRepository;
import com.chavez.repository.TallerRepository;
import com.chavez.service.SagaOrchestrator;
import com.chavez.service.TallerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TallerServiceTest {

    @Mock
    private TallerRepository tallerRepository;

    @Mock
    private TallerAlumnoRepository tallerAlumnoRepository;

    @Mock
    private SagaOrchestrator sagaOrchestrator;

    @InjectMocks
    private TallerServiceImpl service;

    private Taller taller;

    @BeforeEach
    void setUp() {
        taller = new Taller();
        taller.setId(1L);
        taller.setNombre("Taller de Java");
        taller.setDescripcion("Curso avanzado");
        taller.setFechaInicio(LocalDate.of(2025, 1, 1));
        taller.setFechaFin(LocalDate.of(2025, 3, 1));
        taller.setInstructorId(1L);
    }

    @Test
    void obtenerPorId_debeRetornarDTO() {
        when(tallerRepository.findById(1L)).thenReturn(Optional.of(taller));
        when(tallerAlumnoRepository.findByTallerId(1L)).thenReturn(java.util.List.of());
        TallerDTO resultado = service.obtenerPorId(1L);
        assertEquals("Taller de Java", resultado.getNombre());
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion() {
        when(tallerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void crear_debeGuardarYRetornar() {
        when(tallerRepository.save(any(Taller.class))).thenReturn(taller);
        when(tallerAlumnoRepository.findByTallerId(1L)).thenReturn(java.util.List.of());
        TallerDTO dto = new TallerDTO();
        dto.setNombre("Taller de Java");
        dto.setDescripcion("Curso avanzado");
        dto.setInstructorId(1L);
        TallerDTO resultado = service.crear(dto);
        assertNotNull(resultado);
        assertEquals("Taller de Java", resultado.getNombre());
    }
}
