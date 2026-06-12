package com.usco.sgci.dto.convocatoria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ConvocatoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer cuposDisponibles,
        Long estadoId,
        String estadoNombre,
        List<CategoriaConvocatoriaResponse> categorias,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
