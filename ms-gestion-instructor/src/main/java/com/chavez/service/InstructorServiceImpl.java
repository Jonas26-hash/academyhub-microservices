package com.chavez.service;

import com.chavez.dto.InstructorDTO;
import com.chavez.entity.Instructor;
import com.chavez.exception.ResourceNotFoundException;
import com.chavez.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository repository;

    public InstructorServiceImpl(InstructorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InstructorDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public InstructorDTO obtenerPorId(Long id) {
        Instructor instructor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado con id: " + id));
        return toDTO(instructor);
    }

    @Override
    public InstructorDTO crear(InstructorDTO dto) {
        Instructor instructor = toEntity(dto);
        return toDTO(repository.save(instructor));
    }

    @Override
    public InstructorDTO actualizar(Long id, InstructorDTO dto) {
        Instructor instructor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor no encontrado con id: " + id));
        instructor.setNombre(dto.getNombre());
        instructor.setApellido(dto.getApellido());
        instructor.setEmail(dto.getEmail());
        instructor.setTelefono(dto.getTelefono());
        instructor.setEspecialidad(dto.getEspecialidad());
        return toDTO(repository.save(instructor));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Instructor no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    private InstructorDTO toDTO(Instructor instructor) {
        InstructorDTO dto = new InstructorDTO();
        dto.setId(instructor.getId());
        dto.setNombre(instructor.getNombre());
        dto.setApellido(instructor.getApellido());
        dto.setEmail(instructor.getEmail());
        dto.setTelefono(instructor.getTelefono());
        dto.setEspecialidad(instructor.getEspecialidad());
        return dto;
    }

    private Instructor toEntity(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setNombre(dto.getNombre());
        instructor.setApellido(dto.getApellido());
        instructor.setEmail(dto.getEmail());
        instructor.setTelefono(dto.getTelefono());
        instructor.setEspecialidad(dto.getEspecialidad());
        return instructor;
    }
}
