package com.chavez.controller;

import com.chavez.dto.LoginRequest;
import com.chavez.dto.LoginResponse;
import com.chavez.dto.RegisterRequest;
import com.chavez.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        Map<String, Object> resultado = authService.registrar(request);
        if (resultado.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
}
