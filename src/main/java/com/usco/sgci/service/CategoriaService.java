package com.usco.sgci.service;

import com.usco.sgci.dto.categoria.CategoriaCreateRequest;
import com.usco.sgci.dto.categoria.CategoriaResponse;
import com.usco.sgci.dto.categoria.CategoriaUpdateRequest;
import com.usco.sgci.entity.Categoria;
import com.usco.sgci.entity.Estado;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.exception.ResourceNotFoundException;
import com.usco.sgci.repository.CategoriaRepository;
import com.usco.sgci.repository.EstadoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";

    private final CategoriaRepository categoriaRepository;
    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findByEstadoNombreOrderByIdAsc(ESTADO_ACTIVO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long id) {
        return toResponse(buscarCategoriaActiva(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaCreateRequest request) {
        String nombre = limpiar(request.nombre());
        if (categoriaRepository.existsByNombre(nombre)) {
            throw new BusinessException("La categoria ya existe.");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setEstado(buscarEstado(ESTADO_ACTIVO));

        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaUpdateRequest request) {
        Categoria categoria = buscarCategoriaActiva(id);
        String nombre = limpiar(request.nombre());

        if (categoriaRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new BusinessException("La categoria ya existe.");
        }

        categoria.setNombre(nombre);
        return toResponse(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarCategoriaActiva(id);
        categoria.setEstado(buscarEstado(ESTADO_INACTIVO));
    }

    private Categoria buscarCategoriaActiva(Long id) {
        return categoriaRepository.findByIdAndEstadoNombre(id, ESTADO_ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria activa no encontrada."));
    }

    private Estado buscarEstado(String nombre) {
        return estadoRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + nombre + " no encontrado."));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getEstado().getId(),
                categoria.getEstado().getNombre(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
