package com.usco.sgci.dto.reporte;

public record ConvocatoriasCategoriaReporteResponse(
        Long categoriaId,
        String categoriaNombre,
        Long totalConvocatorias
) {
}
