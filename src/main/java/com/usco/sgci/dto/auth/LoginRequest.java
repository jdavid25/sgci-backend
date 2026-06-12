package com.usco.sgci.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String nombreUsuario,

        @NotBlank
        String clave
) {
}
