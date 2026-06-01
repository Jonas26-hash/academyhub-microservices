package com.chavez.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "ms-gestion-alumno", fallbackFactory = AlumnoFeignFallback.class)
public interface AlumnoFeignClient {

    @GetMapping("/alumnos/{id}")
    Map<String, Object> obtenerAlumno(@PathVariable("id") Long id);
}
