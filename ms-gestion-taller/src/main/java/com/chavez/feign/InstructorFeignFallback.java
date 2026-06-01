package com.chavez.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InstructorFeignFallback implements FallbackFactory<InstructorFeignClient> {

    @Override
    public InstructorFeignClient create(Throwable cause) {
        return id -> Map.of(
            "id", id,
            "nombre", "No disponible",
            "apellido", "",
            "email", "",
            "error", "Circuito abierto: " + cause.getMessage()
        );
    }
}
