package com.usco.sgci.service;

import com.usco.sgci.dto.convocatoria.CategoriaConvocatoriaResponse;
import com.usco.sgci.dto.convocatoria.ConvocatoriaCreateRequest;
import com.usco.sgci.dto.convocatoria.ConvocatoriaResponse;
import com.usco.sgci.dto.convocatoria.ConvocatoriaUpdateRequest;
import com.usco.sgci.entity.Categoria;
import com.usco.sgci.entity.Convocatoria;
import com.usco.sgci.entity.ConvocatoriaCategoria;
import com.usco.sgci.entity.ConvocatoriaCategoriaId;
import com.usco.sgci.entity.Estado;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.exception.ResourceNotFoundException;
import com.usco.sgci.repository.CategoriaRepository;
import com.usco.sgci.repository.ConvocatoriaCategoriaRepository;
import com.usco.sgci.repository.ConvocatoriaRepository;
import com.usco.sgci.repository.EstadoRepository;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConvocatoriaService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";
    private static final String ESTADO_BORRADOR = "BORRADOR";
    private static final String ESTADO_PUBLICADA = "PUBLICADA";
    private static final String ESTADO_CERRADA = "CERRADA";

    private static final List<String> ESTADOS_CONVOCATORIA = List.of(
            ESTADO_BORRADOR,
            ESTADO_PUBLICADA,
            ESTADO_CERRADA
    );

    private final ConvocatoriaRepository convocatoriaRepository;
    private final ConvocatoriaCategoriaRepository convocatoriaCategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<ConvocatoriaResponse> listar() {
        return convocatoriaRepository.findByEstadoNombreInOrderByIdAsc(ESTADOS_CONVOCATORIA)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConvocatoriaResponse obtenerPorId(Long id) {
        return toResponse(buscarConvocatoria(id));
    }

    @Transactional
    public ConvocatoriaResponse crear(ConvocatoriaCreateRequest request) {
        validarFechas(request.fechaInicio(), request.fechaFin());

        Convocatoria convocatoria = new Convocatoria();
        convocatoria.setNombre(limpiar(request.nombre()));
        convocatoria.setDescripcion(limpiar(request.descripcion()));
        convocatoria.setFechaInicio(request.fechaInicio());
        convocatoria.setFechaFin(request.fechaFin());
        convocatoria.setCuposDisponibles(request.cuposDisponibles());
        convocatoria.setEstado(buscarEstadoConvocatoria(request.estadoId()));

        Convocatoria guardada = convocatoriaRepository.save(convocatoria);
        reemplazarCategorias(guardada, request.categoriaIds());

        return toResponse(guardada);
    }

    @Transactional
    public ConvocatoriaResponse actualizar(Long id, ConvocatoriaUpdateRequest request) {
        Convocatoria convocatoria = buscarConvocatoria(id);
        validarFechas(request.fechaInicio(), request.fechaFin());

        convocatoria.setNombre(limpiar(request.nombre()));
        convocatoria.setDescripcion(limpiar(request.descripcion()));
        convocatoria.setFechaInicio(request.fechaInicio());
        convocatoria.setFechaFin(request.fechaFin());
        convocatoria.setCuposDisponibles(request.cuposDisponibles());
        convocatoria.setEstado(buscarEstadoConvocatoria(request.estadoId()));

        reemplazarCategorias(convocatoria, request.categoriaIds());

        return toResponse(convocatoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Convocatoria convocatoria = buscarConvocatoria(id);
        convocatoria.setEstado(buscarEstado(ESTADO_INACTIVO));
    }

    private Convocatoria buscarConvocatoria(Long id) {
        return convocatoriaRepository.findByIdAndEstadoNombreIn(id, ESTADOS_CONVOCATORIA)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada."));
    }

    private void reemplazarCategorias(Convocatoria convocatoria, List<Long> categoriaIds) {
        Set<Long> idsUnicos = new LinkedHashSet<>(categoriaIds);
        Estado estadoActivo = buscarEstado(ESTADO_ACTIVO);

        convocatoriaCategoriaRepository.deleteByConvocatoriaId(convocatoria.getId());

        idsUnicos.forEach(categoriaId -> {
            Categoria categoria = categoriaRepository.findByIdAndEstadoNombre(categoriaId, ESTADO_ACTIVO)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria activa no encontrada."));

            ConvocatoriaCategoria relacion = new ConvocatoriaCategoria();
            relacion.setId(new ConvocatoriaCategoriaId(convocatoria.getId(), categoria.getId()));
            relacion.setConvocatoria(convocatoria);
            relacion.setCategoria(categoria);
            relacion.setEstado(estadoActivo);
            convocatoriaCategoriaRepository.save(relacion);
        });
    }

    private Estado buscarEstadoConvocatoria(Long estadoId) {
        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado."));

        if (!ESTADOS_CONVOCATORIA.contains(estado.getNombre())) {
            throw new BusinessException("Estado no valido para convocatoria.");
        }

        return estado;
    }

    private Estado buscarEstado(String nombre) {
        return estadoRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + nombre + " no encontrado."));
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new BusinessException("La fecha fin debe ser mayor o igual a la fecha inicio.");
        }
    }

    private ConvocatoriaResponse toResponse(Convocatoria convocatoria) {
        List<CategoriaConvocatoriaResponse> categorias = convocatoriaCategoriaRepository
                .findByConvocatoriaIdAndEstadoNombre(convocatoria.getId(), ESTADO_ACTIVO)
                .stream()
                .map(relacion -> new CategoriaConvocatoriaResponse(
                        relacion.getCategoria().getId(),
                        relacion.getCategoria().getNombre()
                ))
                .toList();

        return new ConvocatoriaResponse(
                convocatoria.getId(),
                convocatoria.getNombre(),
                convocatoria.getDescripcion(),
                convocatoria.getFechaInicio(),
                convocatoria.getFechaFin(),
                convocatoria.getCuposDisponibles(),
                convocatoria.getEstado().getId(),
                convocatoria.getEstado().getNombre(),
                categorias,
                convocatoria.getCreatedAt(),
                convocatoria.getUpdatedAt()
        );
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
