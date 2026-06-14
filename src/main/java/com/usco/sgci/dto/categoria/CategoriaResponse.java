package com.usco.sgci.dto.categoria;

import java.time.LocalDateTime;

public record CategoriaResponse(
        Long id,
        String nombre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
