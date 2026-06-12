package com.usco.sgci.service;

import com.usco.sgci.dto.rol.RolResponse;
import com.usco.sgci.entity.Rol;
import com.usco.sgci.repository.RolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolService {

    private static final String ESTADO_ACTIVO = "ACTIVO";

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RolResponse> listarActivos() {
        return rolRepository.findByEstadoNombreOrderByIdAsc(ESTADO_ACTIVO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RolResponse toResponse(Rol rol) {
        return new RolResponse(
                rol.getId(),
                rol.getNombre(),
                rol.getEstado().getId(),
                rol.getEstado().getNombre()
        );
    }
}
