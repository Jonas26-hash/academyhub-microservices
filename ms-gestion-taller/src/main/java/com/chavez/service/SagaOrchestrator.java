package com.chavez.service;

import com.chavez.entity.SagaLog;
import com.chavez.entity.Taller;
import com.chavez.entity.TallerAlumno;
import com.chavez.exception.ResourceNotFoundException;
import com.chavez.feign.AlumnoFeignClient;
import com.chavez.repository.SagaLogRepository;
import com.chavez.repository.TallerAlumnoRepository;
import com.chavez.repository.TallerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(SagaOrchestrator.class);

    private final SagaLogRepository sagaLogRepository;
    private final TallerRepository tallerRepository;
    private final TallerAlumnoRepository tallerAlumnoRepository;
    private final AlumnoFeignClient alumnoFeign;

    public SagaOrchestrator(SagaLogRepository sagaLogRepository,
                            TallerRepository tallerRepository,
                            TallerAlumnoRepository tallerAlumnoRepository,
                            AlumnoFeignClient alumnoFeign) {
        this.sagaLogRepository = sagaLogRepository;
        this.tallerRepository = tallerRepository;
        this.tallerAlumnoRepository = tallerAlumnoRepository;
        this.alumnoFeign = alumnoFeign;
    }

    @Transactional
    public void inscribirAlumnoConSaga(Long tallerId, Long alumnoId) {
        SagaLog saga = new SagaLog(SagaLog.Tipo.INSCRIPCION_ALUMNO, tallerId, alumnoId);
        saga = sagaLogRepository.save(saga);

        try {
            Taller taller = tallerRepository.findById(tallerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Taller no encontrado: " + tallerId));

            if (tallerAlumnoRepository.existsByTallerYAlumno(tallerId, alumnoId)) {
                throw new RuntimeException("El alumno ya está inscrito en este taller");
            }

            var alumnoData = alumnoFeign.obtenerAlumno(alumnoId);
            if (alumnoData == null || alumnoData.containsKey("error")) {
                throw new RuntimeException("Alumno no válido o servicio no disponible");
            }

            tallerAlumnoRepository.save(new TallerAlumno(taller, alumnoId));

            saga.setEstado(SagaLog.Estado.COMPLETED);
            saga.setCompletadoEn(LocalDateTime.now());
            sagaLogRepository.save(saga);

            log.info("Saga COMPLETED: inscripción taller={} alumno={}", tallerId, alumnoId);

        } catch (Exception e) {
            log.error("Saga FAILED: inscripción taller={} alumno={} error={}",
                    tallerId, alumnoId, e.getMessage());
            saga.setEstado(SagaLog.Estado.FAILED);
            saga.setErrorMensaje(e.getMessage());
            sagaLogRepository.save(saga);

            compensarInscripcion(saga);
            throw new RuntimeException("Error al inscribir alumno: " + e.getMessage());
        }
    }

    @Transactional
    public void compensarInscripcion(SagaLog saga) {
        try {
            tallerAlumnoRepository.deleteByTallerYAlumno(saga.getTallerId(), saga.getAlumnoId());
            saga.setEstado(SagaLog.Estado.COMPENSATED);
            sagaLogRepository.save(saga);
            log.info("Saga COMPENSATED: taller={} alumno={}", saga.getTallerId(), saga.getAlumnoId());
        } catch (Exception e) {
            log.error("Compensación falló para saga {}: {}", saga.getId(), e.getMessage());
        }
    }
}
