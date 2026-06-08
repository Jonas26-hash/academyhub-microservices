package com.chavez.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, message = "El username debe tener al menos 3 caracteres")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 4, message = "El password debe tener al menos 4 caracteres")
    private String password;

    private String rol = "ALUMNO";

    public void setRol(String rol) {
        this.rol = rol != null ? rol.toUpperCase() : "ALUMNO";
    }
}
