package com.usco.sgci.service;

import com.usco.sgci.dto.usuario.UsuarioCreateRequest;
import com.usco.sgci.dto.usuario.UsuarioResponse;
import com.usco.sgci.dto.usuario.UsuarioUpdateRequest;
import com.usco.sgci.entity.Estado;
import com.usco.sgci.entity.Rol;
import com.usco.sgci.entity.Usuario;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.exception.ResourceNotFoundException;
import com.usco.sgci.repository.EstadoRepository;
import com.usco.sgci.repository.RolRepository;
import com.usco.sgci.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findByEstadoNombreOrderByIdAsc(ESTADO_ACTIVO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return toResponse(buscarUsuario(id));
    }

    @Transactional
    public UsuarioResponse crear(UsuarioCreateRequest request) {
        validarUnicosCrear(request);

        Rol rol = buscarRolActivo(request.rolId());
        Estado estado = buscarEstadoActivo(request.estadoId());

        Usuario usuario = new Usuario();
        usuario.setIdentificacion(limpiar(request.identificacion()));
        usuario.setNombre(limpiar(request.nombre()));
        usuario.setCorreo(limpiar(request.correo()).toLowerCase());
        usuario.setNombreUsuario(limpiar(request.nombreUsuario()));
        usuario.setClave(passwordEncoder.encode(request.clave()));
        usuario.setRol(rol);
        usuario.setEstado(estado);

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarUsuario(id);
        validarUnicosActualizar(id, request);
        Rol rol = buscarRolActivo(request.rolId());
        Estado estado = buscarEstadoActivo(request.estadoId());

        usuario.setIdentificacion(limpiar(request.identificacion()));
        usuario.setNombre(limpiar(request.nombre()));
        usuario.setCorreo(limpiar(request.correo()).toLowerCase());
        usuario.setNombreUsuario(limpiar(request.nombreUsuario()));
        usuario.setRol(rol);
        usuario.setEstado(estado);

        if (request.clave() != null && !request.clave().isBlank()) {
            usuario.setClave(passwordEncoder.encode(request.clave()));
        }

        return toResponse(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscarUsuario(id);
        Estado estadoInactivo = estadoRepository.findByNombre(ESTADO_INACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Estado INACTIVO no encontrado."));

        usuario.setEstado(estadoInactivo);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findByIdAndEstadoNombre(id, ESTADO_ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario activo no encontrado."));
    }

    private Rol buscarRolActivo(Long id) {
        return rolRepository.findByIdAndEstadoNombre(id, ESTADO_ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Rol activo no encontrado."));
    }

    private Estado buscarEstadoActivo(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado."));
        if (!ESTADO_ACTIVO.equals(estado.getNombre())) {
            throw new BusinessException("El usuario debe quedar en estado ACTIVO.");
        }
        return estado;
    }

    private void validarUnicosCrear(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByIdentificacion(limpiar(request.identificacion()))) {
            throw new BusinessException("La identificacion ya existe.");
        }
        if (usuarioRepository.existsByCorreo(limpiar(request.correo()).toLowerCase())) {
            throw new BusinessException("El correo ya existe.");
        }
        if (usuarioRepository.existsByNombreUsuario(limpiar(request.nombreUsuario()))) {
            throw new BusinessException("El nombre de usuario ya existe.");
        }
    }

    private void validarUnicosActualizar(Long id, UsuarioUpdateRequest request) {
        if (usuarioRepository.existsByIdentificacionAndIdNot(limpiar(request.identificacion()), id)) {
            throw new BusinessException("La identificacion ya existe.");
        }
        if (usuarioRepository.existsByCorreoAndIdNot(limpiar(request.correo()).toLowerCase(), id)) {
            throw new BusinessException("El correo ya existe.");
        }
        if (usuarioRepository.existsByNombreUsuarioAndIdNot(limpiar(request.nombreUsuario()), id)) {
            throw new BusinessException("El nombre de usuario ya existe.");
        }
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getIdentificacion(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getNombreUsuario(),
                usuario.getRol().getId(),
                usuario.getRol().getNombre(),
                usuario.getEstado().getId(),
                usuario.getEstado().getNombre(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
