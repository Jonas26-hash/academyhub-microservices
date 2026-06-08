package com.chavez.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor
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

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        if (!ROLES_VALIDOS.contains(rol.toUpperCase())) {
            throw new IllegalArgumentException("Rol inv\u00e1lido: " + rol + ". V\u00e1lidos: " + ROLES_VALIDOS);
        }
        this.rol = rol.toUpperCase();
    }

    public void setRol(String rol) {
        this.rol = rol != null ? rol.toUpperCase() : "ALUMNO";
    }
}
