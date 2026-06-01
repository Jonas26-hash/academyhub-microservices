package com.chavez.service;

import com.chavez.dto.LoginResponse;
import com.chavez.entity.Usuario;
import com.chavez.repository.UsuarioRepository;
import com.chavez.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Map<String, Object> registrar(String username, String password) {
        if (usuarioRepository.existsByUsername(username)) {
            return Map.of("error", "El usuario ya existe");
        }
        Usuario usuario = new Usuario(username, passwordEncoder.encode(password), "USER");
        usuarioRepository.save(usuario);
        String token = jwtUtil.generarToken(username, "USER");
        return Map.of("mensaje", "Usuario registrado exitosamente",
                      "username", username,
                      "token", token);
    }

    public LoginResponse login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        String token = jwtUtil.generarToken(username, usuario.getRol());
        return new LoginResponse(token);
    }
}
