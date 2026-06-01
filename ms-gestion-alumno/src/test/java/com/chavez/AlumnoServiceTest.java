package com.chavez;

import com.chavez.dto.AlumnoDTO;
import com.chavez.entity.Alumno;
import com.chavez.exception.ResourceNotFoundException;
import com.chavez.repository.AlumnoRepository;
import com.chavez.service.AlumnoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {

    @Mock
    private AlumnoRepository repository;

    @InjectMocks
    private AlumnoServiceImpl service;

    private Alumno alumno;

    @BeforeEach
    void setUp() {
        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("Maria");
        alumno.setApellido("Lopez");
        alumno.setEmail("maria@test.com");
        alumno.setTelefono("111222333");
        alumno.setFechaNacimiento(LocalDate.of(2000, 1, 1));
    }

    @Test
    void listarTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(alumno));
        List<AlumnoDTO> resultado = service.listarTodos();
        assertEquals(1, resultado.size());
        assertEquals("Maria", resultado.get(0).getNombre());
    }

    @Test
    void obtenerPorId_debeRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(alumno));
        AlumnoDTO resultado = service.obtenerPorId(1L);
        assertEquals("Maria", resultado.getNombre());
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void crear_debeGuardarYRetornar() {
        when(repository.save(any(Alumno.class))).thenReturn(alumno);
        AlumnoDTO dto = new AlumnoDTO();
        dto.setNombre("Maria");
        dto.setApellido("Lopez");
        dto.setEmail("maria@test.com");
        AlumnoDTO resultado = service.crear(dto);
        assertNotNull(resultado);
        assertEquals("Maria", resultado.getNombre());
    }
}
