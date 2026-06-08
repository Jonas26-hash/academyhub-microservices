package com.chavez.service;

import com.chavez.dto.TallerDTO;
import com.chavez.entity.Taller;
import com.chavez.entity.TallerAlumno;
import com.chavez.exception.ResourceNotFoundException;
import com.chavez.feign.AlumnoFeignClient;
import com.chavez.feign.InstructorFeignClient;
import com.chavez.repository.TallerAlumnoRepository;
import com.chavez.repository.TallerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TallerServiceImpl implements TallerService {

    private final TallerRepository tallerRepository;
    private final TallerAlumnoRepository tallerAlumnoRepository;
    private final InstructorFeignClient instructorFeign;
    private final AlumnoFeignClient alumnoFeign;
    private final SagaOrchestrator sagaOrchestrator;

    public TallerServiceImpl(TallerRepository tallerRepository,
                             TallerAlumnoRepository tallerAlumnoRepository,
                             InstructorFeignClient instructorFeign,
                             AlumnoFeignClient alumnoFeign,
                             SagaOrchestrator sagaOrchestrator) {
        this.tallerRepository = tallerRepository;
        this.tallerAlumnoRepository = tallerAlumnoRepository;
        this.instructorFeign = instructorFeign;
        this.alumnoFeign = alumnoFeign;
        this.sagaOrchestrator = sagaOrchestrator;
    }

    @Override
    public List<TallerDTO> listarTodos() {
        return tallerRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public TallerDTO obtenerPorId(Long id) {
        Taller taller = tallerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Taller no encontrado con id: " + id));
        return toDTO(taller);
    }

    @Override
    public TallerDTO crear(TallerDTO dto) {
        Taller taller = toEntity(dto);
        Taller guardado = tallerRepository.save(taller);
        return toDTO(guardado);
    }

    @Override
    public TallerDTO actualizar(Long id, TallerDTO dto) {
        Taller taller = tallerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Taller no encontrado con id: " + id));
        taller.setNombre(dto.getNombre());
        taller.setDescripcion(dto.getDescripcion());
        taller.setFechaInicio(dto.getFechaInicio());
        taller.setFechaFin(dto.getFechaFin());
        taller.setInstructorId(dto.getInstructorId());
        taller.setCupo(dto.getCupo());
        return toDTO(tallerRepository.save(taller));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!tallerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Taller no encontrado con id: " + id);
        }
        tallerAlumnoRepository.findByTallerId(id).forEach(ta ->
                tallerAlumnoRepository.delete(ta));
        tallerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void inscribirAlumno(Long tallerId, Long alumnoId) {
        validarCupo(tallerId);
        sagaOrchestrator.inscribirAlumnoConSaga(tallerId, alumnoId);
    }

    private void validarCupo(Long tallerId) {
        Taller taller = tallerRepository.findById(tallerId)
                .orElseThrow(() -> new ResourceNotFoundException("Taller no encontrado con id: " + tallerId));
        int inscritos = tallerAlumnoRepository.countByTallerId(tallerId);
        if (inscritos >= taller.getCupo()) {
            throw new RuntimeException("Cupo lleno: el taller '" + taller.getNombre()
                    + "' tiene cupo m�ximo de " + taller.getCupo() + " alumnos");
        }
    }

    @Override
    @Transactional
    public void desinscribirAlumno(Long tallerId, Long alumnoId) {
        if (!tallerAlumnoRepository.existsByTallerYAlumno(tallerId, alumnoId)) {
            throw new ResourceNotFoundException("El alumno no est� inscrito en este taller");
        }
        tallerAlumnoRepository.deleteByTallerYAlumno(tallerId, alumnoId);
    }

    @Override
    public Map<String, Object> obtenerInstructorDelTaller(Long tallerId) {
        Taller taller = tallerRepository.findById(tallerId)
                .orElseThrow(() -> new ResourceNotFoundException("Taller no encontrado con id: " + tallerId));
        return instructorFeign.obtenerInstructor(taller.getInstructorId());
    }

    @Override
    public List<Map<String, Object>> obtenerAlumnosDelTaller(Long tallerId) {
        if (!tallerRepository.existsById(tallerId)) {
            throw new ResourceNotFoundException("Taller no encontrado con id: " + tallerId);
        }
        return tallerAlumnoRepository.findByTallerId(tallerId).stream()
                .map(ta -> alumnoFeign.obtenerAlumno(ta.getId().getAlumnoId()))
                .toList();
    }

    @Override
    public List<TallerDTO> listarPorAlumnoId(Long alumnoId) {
        return tallerAlumnoRepository.findByAlumnoId(alumnoId).stream()
                .map(ta -> tallerRepository.findById(ta.getId().getTallerId()).orElse(null))
                .filter(t -> t != null)
                .map(this::toDTO)
                .toList();
    }

    private TallerDTO toDTO(Taller taller) {
        TallerDTO dto = new TallerDTO();
        dto.setId(taller.getId());
        dto.setNombre(taller.getNombre());
        dto.setDescripcion(taller.getDescripcion());
        dto.setFechaInicio(taller.getFechaInicio());
        dto.setFechaFin(taller.getFechaFin());
        dto.setCupo(taller.getCupo());

        List<Long> alumnoIds = tallerAlumnoRepository.findByTallerId(taller.getId())
                .stream().map(ta -> ta.getId().getAlumnoId()).toList();

        if (taller.getInstructorId() != null) {
            try {
                dto.setInstructor(instructorFeign.obtenerInstructor(taller.getInstructorId()));
            } catch (Exception e) {
                dto.setInstructor(Map.of("error", "Instructor no disponible"));
            }
        }

        if (!alumnoIds.isEmpty()) {
            try {
                List<Map<String, Object>> alumnos = alumnoIds.stream()
                        .<Map<String, Object>>map(id -> {
                            try { return alumnoFeign.obtenerAlumno(id); }
                            catch (Exception e) { return Map.of("id", (Object) id, "error", (Object) "No disponible"); }
                        })
                        .toList();
                dto.setAlumnos(alumnos);
            } catch (Exception e) {
                dto.setAlumnos(List.of(Map.of("error", (Object) "Alumnos no disponibles")));
            }
        } else {
            dto.setAlumnos(List.of());
        }

        return dto;
    }

    private Taller toEntity(TallerDTO dto) {
        Taller taller = new Taller();
        taller.setNombre(dto.getNombre());
        taller.setDescripcion(dto.getDescripcion());
        taller.setFechaInicio(dto.getFechaInicio());
        taller.setFechaFin(dto.getFechaFin());
        taller.setInstructorId(dto.getInstructorId());
        taller.setCupo(dto.getCupo() != null ? dto.getCupo() : 30);
        return taller;
    }
}
