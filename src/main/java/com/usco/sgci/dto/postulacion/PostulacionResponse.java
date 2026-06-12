package com.usco.sgci.dto.postulacion;

import java.time.LocalDateTime;

public record PostulacionResponse(
        Long id,
        Long convocatoriaId,
        String convocatoriaNombre,
        Long usuarioId,
        String usuarioNombre,
        Long estadoId,
        String estadoNombre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
