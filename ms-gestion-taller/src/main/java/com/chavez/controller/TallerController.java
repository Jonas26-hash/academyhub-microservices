package com.chavez.controller;

import com.chavez.dto.TallerDTO;
import com.chavez.service.PdfExportService;
import com.chavez.service.TallerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/talleres")
public class TallerController {

    private final TallerService service;
    private final PdfExportService pdfExportService;

    public TallerController(TallerService service, PdfExportService pdfExportService) {
        this.service = service;
        this.pdfExportService = pdfExportService;
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
    public ResponseEntity<TallerDTO> crear(@Valid @RequestBody TallerDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TallerDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TallerDTO dto) {
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

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<TallerDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(service.listarPorAlumnoId(alumnoId));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportarPDF() {
        List<TallerDTO> talleres = service.listarTodos();
        byte[] pdf = pdfExportService.exportarTalleresPDF(talleres);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "talleres-reporte.pdf");
        headers.setContentLength(pdf.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
