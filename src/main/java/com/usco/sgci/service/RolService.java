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

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RolResponse> listar() {
        return rolRepository.findByDeletedAtIsNullOrderByIdAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RolResponse toResponse(Rol rol) {
        return new RolResponse(
                rol.getId(),
                rol.getNombre()
        );
    }
}
