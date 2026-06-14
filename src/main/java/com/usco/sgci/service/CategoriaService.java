package com.usco.sgci.service;

import com.usco.sgci.dto.categoria.CategoriaCreateRequest;
import com.usco.sgci.dto.categoria.CategoriaResponse;
import com.usco.sgci.dto.categoria.CategoriaUpdateRequest;
import com.usco.sgci.entity.Categoria;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.exception.ResourceNotFoundException;
import com.usco.sgci.repository.CategoriaRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findByDeletedAtIsNullOrderByIdAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long id) {
        return toResponse(buscarCategoria(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaCreateRequest request) {
        String nombre = limpiar(request.nombre());
        if (categoriaRepository.existsByNombreAndDeletedAtIsNull(nombre)) {
            throw new BusinessException("La categoria ya existe.");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);

        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaUpdateRequest request) {
        Categoria categoria = buscarCategoria(id);
        String nombre = limpiar(request.nombre());

        if (categoriaRepository.existsByNombreAndIdNotAndDeletedAtIsNull(nombre, id)) {
            throw new BusinessException("La categoria ya existe.");
        }

        categoria.setNombre(nombre);
        return toResponse(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarCategoria(id);
        categoria.setDeletedAt(LocalDateTime.now(ZoneOffset.UTC));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada."));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getCreatedAt(),
                categoria.getUpdatedAt()
        );
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
