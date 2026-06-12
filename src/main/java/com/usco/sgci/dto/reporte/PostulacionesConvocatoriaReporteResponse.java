package com.usco.sgci.dto.reporte;

public record PostulacionesConvocatoriaReporteResponse(
        Long convocatoriaId,
        String convocatoriaNombre,
        Long totalPostulaciones
) {
}
