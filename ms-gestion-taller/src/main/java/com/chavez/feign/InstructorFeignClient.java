package com.chavez.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "ms-gestion-instructor", fallbackFactory = InstructorFeignFallback.class)
public interface InstructorFeignClient {

    @GetMapping("/instructores/{id}")
    Map<String, Object> obtenerInstructor(@PathVariable("id") Long id);
}
