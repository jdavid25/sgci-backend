package com.usco.sgci.service;

import com.usco.sgci.dto.reporte.ConvocatoriasCategoriaReporteResponse;
import com.usco.sgci.dto.reporte.PostulacionesConvocatoriaReporteResponse;
import com.usco.sgci.dto.reporte.ResultadoPostulacionesReporteResponse;
import com.usco.sgci.repository.ConvocatoriaCategoriaRepository;
import com.usco.sgci.repository.PostulacionRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_BORRADOR = "BORRADOR";
    private static final String ESTADO_PUBLICADA = "PUBLICADA";
    private static final String ESTADO_CERRADA = "CERRADA";
    private static final String ESTADO_APROBADA = "APROBADA";
    private static final String ESTADO_RECHAZADA = "RECHAZADA";

    private static final List<String> ESTADOS_CONVOCATORIA_VISIBLES = List.of(
            ESTADO_BORRADOR,
            ESTADO_PUBLICADA,
            ESTADO_CERRADA
    );
    private static final List<String> ESTADOS_RESULTADO = List.of(ESTADO_APROBADA, ESTADO_RECHAZADA);

    private final ConvocatoriaCategoriaRepository convocatoriaCategoriaRepository;
    private final PostulacionRepository postulacionRepository;

    @Transactional(readOnly = true)
    public List<ConvocatoriasCategoriaReporteResponse> convocatoriasPorCategoria() {
        return convocatoriaCategoriaRepository.contarConvocatoriasPorCategoria(
                ESTADO_ACTIVO,
                ESTADOS_CONVOCATORIA_VISIBLES
        );
    }

    @Transactional(readOnly = true)
    public List<PostulacionesConvocatoriaReporteResponse> postulacionesPorConvocatoria() {
        return postulacionRepository.contarPostulacionesPorConvocatoria(ESTADOS_CONVOCATORIA_VISIBLES);
    }

    @Transactional(readOnly = true)
    public List<ResultadoPostulacionesReporteResponse> resultadoPostulaciones() {
        Map<String, ResultadoPostulacionesReporteResponse> resultados = postulacionRepository
                .contarResultadosPostulaciones(ESTADOS_RESULTADO, ESTADOS_CONVOCATORIA_VISIBLES)
                .stream()
                .collect(Collectors.toMap(ResultadoPostulacionesReporteResponse::estadoNombre, Function.identity()));

        return ESTADOS_RESULTADO.stream()
                .map(estado -> resultados.getOrDefault(estado, new ResultadoPostulacionesReporteResponse(estado, 0L)))
                .toList();
    }
}
