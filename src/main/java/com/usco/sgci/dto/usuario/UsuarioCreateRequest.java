package com.usco.sgci.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequest(
        @NotBlank
        @Size(max = 30)
        String identificacion,

        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotBlank
        @Email
        @Size(max = 150)
        String correo,

        @NotBlank
        @Size(max = 80)
        String nombreUsuario,

        @NotBlank
        @Size(min = 8, max = 100)
        String clave,

        @NotNull
        @Positive
        Long rolId,

        @NotNull
        @Positive
        Long estadoId
) {
}
