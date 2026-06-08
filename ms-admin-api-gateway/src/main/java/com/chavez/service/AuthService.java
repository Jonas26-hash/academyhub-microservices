package com.chavez.service;

import com.chavez.dto.LoginResponse;
import com.chavez.dto.RegisterRequest;
import com.chavez.entity.Usuario;
import com.chavez.repository.UsuarioRepository;
import com.chavez.util.JwtUtil;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void crearAdminPorDefecto() {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario("admin", passwordEncoder.encode("admin123"), "ADMIN");
            usuarioRepository.save(admin);
        }
    }

    @Transactional
    public Map<String, Object> registrar(RegisterRequest request) {
        String username = request.getUsername().trim();
        String rol = request.getRol().toUpperCase();

        if (!Usuario.ROLES_VALIDOS.contains(rol)) {
            return Map.of("error", "Rol inv\u00e1lido. V\u00e1lidos: " + Usuario.ROLES_VALIDOS);
        }
        if (usuarioRepository.existsByUsername(username)) {
            return Map.of("error", "El usuario ya existe");
        }

        Usuario usuario = new Usuario(username, passwordEncoder.encode(request.getPassword()), rol);
        usuarioRepository.save(usuario);
        String token = jwtUtil.generarToken(username, rol);
        return Map.of("mensaje", "Usuario registrado exitosamente",
                      "username", username,
                      "rol", rol,
                      "token", token);
    }

    public LoginResponse login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        String token = jwtUtil.generarToken(username, usuario.getRol());
        return new LoginResponse(token, usuario.getRol());
    }
}
