package com.chavez.controller;

import com.chavez.dto.TallerDTO;
import com.chavez.service.TallerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/talleres")
public class TallerController {

    private final TallerService service;

    public TallerController(TallerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TallerDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TallerDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TallerDTO> crear(@RequestBody TallerDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TallerDTO> actualizar(@PathVariable Long id, @RequestBody TallerDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/alumnos/{alumnoId}")
    public ResponseEntity<Void> inscribirAlumno(@PathVariable Long id, @PathVariable Long alumnoId) {
        service.inscribirAlumno(id, alumnoId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/alumnos/{alumnoId}")
    public ResponseEntity<Void> desinscribirAlumno(@PathVariable Long id, @PathVariable Long alumnoId) {
        service.desinscribirAlumno(id, alumnoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/instructor")
    public ResponseEntity<Map<String, Object>> obtenerInstructor(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerInstructorDelTaller(id));
    }

    @GetMapping("/{id}/alumnos")
    public ResponseEntity<List<Map<String, Object>>> obtenerAlumnos(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerAlumnosDelTaller(id));
    }
}
