package com.chavez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AlumnoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlumnoApplication.class, args);
    }
}
