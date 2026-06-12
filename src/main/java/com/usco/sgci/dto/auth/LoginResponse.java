package com.usco.sgci.dto.auth;

public record LoginResponse(
        String token,
        Long usuarioId,
        String nombre,
        String rol
) {
}
