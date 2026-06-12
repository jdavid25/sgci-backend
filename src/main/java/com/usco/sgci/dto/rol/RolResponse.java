package com.usco.sgci.dto.rol;

public record RolResponse(
        Long id,
        String nombre,
        Long estadoId,
        String estadoNombre
) {
}
