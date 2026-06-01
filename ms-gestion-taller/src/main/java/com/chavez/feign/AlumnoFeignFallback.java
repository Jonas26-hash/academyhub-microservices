package com.chavez.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AlumnoFeignFallback implements FallbackFactory<AlumnoFeignClient> {

    @Override
    public AlumnoFeignClient create(Throwable cause) {
        return id -> Map.of(
            "id", id,
            "nombre", "No disponible",
            "apellido", "",
            "email", "",
            "error", "Circuito abierto: " + cause.getMessage()
        );
    }
}
