package com.usco.sgci.service;

import com.usco.sgci.dto.estado.EstadoResponse;
import com.usco.sgci.entity.Estado;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.repository.EstadoRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private static final Set<String> TIPOS_VALIDOS = Set.of("GENERAL", "CONVOCATORIA", "POSTULACION");

    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<EstadoResponse> listarPorTipo(String tipo) {
        String tipoNormalizado = normalizarTipo(tipo);

        return estadoRepository.findByTipoOrderByIdAsc(tipoNormalizado)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new BusinessException("Debe indicar el tipo de estado.");
        }

        String tipoNormalizado = tipo.trim().toUpperCase();
        if (!TIPOS_VALIDOS.contains(tipoNormalizado)) {
            throw new BusinessException("Tipo de estado no valido.");
        }

        return tipoNormalizado;
    }

    private EstadoResponse toResponse(Estado estado) {
        return new EstadoResponse(
                estado.getId(),
                estado.getNombre(),
                estado.getTipo()
        );
    }
}
