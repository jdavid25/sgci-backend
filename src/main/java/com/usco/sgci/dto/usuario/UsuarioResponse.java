package com.usco.sgci.dto.usuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String identificacion,
        String nombre,
        String correo,
        String nombreUsuario,
        Long rolId,
        String rolNombre,
        Long estadoId,
        String estadoNombre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
