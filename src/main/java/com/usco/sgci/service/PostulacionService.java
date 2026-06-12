package com.usco.sgci.service;

import com.usco.sgci.dto.postulacion.PostulacionCreateRequest;
import com.usco.sgci.dto.postulacion.PostulacionEstadoRequest;
import com.usco.sgci.dto.postulacion.PostulacionResponse;
import com.usco.sgci.entity.Convocatoria;
import com.usco.sgci.entity.Estado;
import com.usco.sgci.entity.Postulacion;
import com.usco.sgci.entity.Usuario;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.exception.ResourceNotFoundException;
import com.usco.sgci.repository.ConvocatoriaRepository;
import com.usco.sgci.repository.EstadoRepository;
import com.usco.sgci.repository.PostulacionRepository;
import com.usco.sgci.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostulacionService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_PUBLICADA = "PUBLICADA";
    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_APROBADA = "APROBADA";
    private static final String ESTADO_RECHAZADA = "RECHAZADA";
    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";

    private static final List<String> ESTADOS_QUE_OCUPAN_CUPO = List.of(ESTADO_PENDIENTE, ESTADO_APROBADA);
    private static final List<String> ESTADOS_RESULTADO = List.of(ESTADO_APROBADA, ESTADO_RECHAZADA);

    private final PostulacionRepository postulacionRepository;
    private final ConvocatoriaRepository convocatoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;

    @Transactional
    public PostulacionResponse crear(PostulacionCreateRequest request) {
        Usuario usuario = buscarUsuarioAutenticado();
        validarRolEstudiante(usuario);

        Convocatoria convocatoria = convocatoriaRepository
                .findByIdAndEstadoNombreIn(request.convocatoriaId(), List.of(ESTADO_PUBLICADA))
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria publicada no encontrada."));

        validarFechasConvocatoria(convocatoria);
        validarPostulacionDuplicada(convocatoria.getId(), usuario.getId());
        validarCuposDisponibles(convocatoria);

        Postulacion postulacion = new Postulacion();
        postulacion.setConvocatoria(convocatoria);
        postulacion.setUsuario(usuario);
        postulacion.setEstado(buscarEstado(ESTADO_PENDIENTE));

        return toResponse(postulacionRepository.save(postulacion));
    }

    @Transactional(readOnly = true)
    public List<PostulacionResponse> listar() {
        return postulacionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostulacionResponse> listarMisPostulaciones() {
        Usuario usuario = buscarUsuarioAutenticado();
        return postulacionRepository.findByUsuarioIdOrderByIdAsc(usuario.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PostulacionResponse cambiarEstado(Long id, PostulacionEstadoRequest request) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulacion no encontrada."));

        Estado estado = estadoRepository.findById(request.estadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado."));

        if (!ESTADOS_RESULTADO.contains(estado.getNombre())) {
            throw new BusinessException("El estado de postulacion debe ser APROBADA o RECHAZADA.");
        }

        postulacion.setEstado(estado);
        return toResponse(postulacion);
    }

    private Usuario buscarUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("Usuario autenticado no encontrado.");
        }

        return usuarioRepository.findByNombreUsuarioAndEstadoNombre(authentication.getName(), ESTADO_ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado."));
    }

    private void validarRolEstudiante(Usuario usuario) {
        if (!ROL_ESTUDIANTE.equals(usuario.getRol().getNombre())) {
            throw new BusinessException("Solo los estudiantes pueden postularse.");
        }
    }

    private void validarFechasConvocatoria(Convocatoria convocatoria) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(convocatoria.getFechaInicio()) || hoy.isAfter(convocatoria.getFechaFin())) {
            throw new BusinessException("La convocatoria no esta dentro del rango de fechas permitido para postulacion.");
        }
    }

    private void validarPostulacionDuplicada(Long convocatoriaId, Long usuarioId) {
        if (postulacionRepository.existsByConvocatoriaIdAndUsuarioId(convocatoriaId, usuarioId)) {
            throw new BusinessException("El estudiante ya se postulo a esta convocatoria.");
        }
    }

    private void validarCuposDisponibles(Convocatoria convocatoria) {
        long cuposOcupados = postulacionRepository.countByConvocatoriaIdAndEstadoNombreIn(
                convocatoria.getId(),
                ESTADOS_QUE_OCUPAN_CUPO
        );

        if (cuposOcupados >= convocatoria.getCuposDisponibles()) {
            throw new BusinessException("La convocatoria no tiene cupos disponibles.");
        }
    }

    private Estado buscarEstado(String nombre) {
        return estadoRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Estado " + nombre + " no encontrado."));
    }

    private PostulacionResponse toResponse(Postulacion postulacion) {
        return new PostulacionResponse(
                postulacion.getId(),
                postulacion.getConvocatoria().getId(),
                postulacion.getConvocatoria().getNombre(),
                postulacion.getUsuario().getId(),
                postulacion.getUsuario().getNombre(),
                postulacion.getEstado().getId(),
                postulacion.getEstado().getNombre(),
                postulacion.getCreatedAt(),
                postulacion.getUpdatedAt()
        );
    }
}
