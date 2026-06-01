package com.chavez.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    public static final Set<String> ROLES_VALIDOS = Set.of("ALUMNO", "INSTRUCTOR", "ADMIN");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol = "ALUMNO";

    public Usuario() {}

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        if (!ROLES_VALIDOS.contains(rol.toUpperCase())) {
            throw new IllegalArgumentException("Rol inv�lido: " + rol + ". V�lidos: " + ROLES_VALIDOS);
        }
        this.rol = rol.toUpperCase();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol.toUpperCase(); }
}
