package com.usco.sgci.dto.categoria;

import java.time.LocalDateTime;

public record CategoriaResponse(
        Long id,
        String nombre,
        Long estadoId,
        String estadoNombre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
