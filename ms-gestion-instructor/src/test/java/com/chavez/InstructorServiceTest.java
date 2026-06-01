package com.chavez;

import com.chavez.dto.InstructorDTO;
import com.chavez.entity.Instructor;
import com.chavez.exception.ResourceNotFoundException;
import com.chavez.repository.InstructorRepository;
import com.chavez.service.InstructorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository repository;

    @InjectMocks
    private InstructorServiceImpl service;

    private Instructor instructor;
    private InstructorDTO dto;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setNombre("Juan");
        instructor.setApellido("Perez");
        instructor.setEmail("juan@test.com");
        instructor.setTelefono("999888777");
        instructor.setEspecialidad("Matematicas");

        dto = new InstructorDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("juan@test.com");
        dto.setTelefono("999888777");
        dto.setEspecialidad("Matematicas");
    }

    @Test
    void listarTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(instructor));
        List<InstructorDTO> resultado = service.listarTodos();
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void obtenerPorId_debeRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(instructor));
        InstructorDTO resultado = service.obtenerPorId(1L);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void crear_debeGuardarYRetornar() {
        when(repository.save(any(Instructor.class))).thenReturn(instructor);
        InstructorDTO resultado = service.crear(dto);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void eliminar_debeLanzarExcepcionSiNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }
}
