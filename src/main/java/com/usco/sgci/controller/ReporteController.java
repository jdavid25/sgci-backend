package com.usco.sgci.controller;

import com.usco.sgci.dto.reporte.ConvocatoriasCategoriaReporteResponse;
import com.usco.sgci.dto.reporte.PostulacionesConvocatoriaReporteResponse;
import com.usco.sgci.dto.reporte.ResultadoPostulacionesReporteResponse;
import com.usco.sgci.service.ReporteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/convocatorias-categoria")
    public List<ConvocatoriasCategoriaReporteResponse> convocatoriasPorCategoria() {
        return reporteService.convocatoriasPorCategoria();
    }

    @GetMapping("/postulaciones-convocatoria")
    public List<PostulacionesConvocatoriaReporteResponse> postulacionesPorConvocatoria() {
        return reporteService.postulacionesPorConvocatoria();
    }

    @GetMapping("/resultado-postulaciones")
    public List<ResultadoPostulacionesReporteResponse> resultadoPostulaciones() {
        return reporteService.resultadoPostulaciones();
    }
}
