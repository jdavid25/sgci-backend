package com.usco.sgci.service;

import com.usco.sgci.dto.usuario.UsuarioCreateRequest;
import com.usco.sgci.dto.usuario.UsuarioResponse;
import com.usco.sgci.dto.usuario.UsuarioUpdateRequest;
import com.usco.sgci.entity.Rol;
import com.usco.sgci.entity.Usuario;
import com.usco.sgci.exception.BusinessException;
import com.usco.sgci.exception.ResourceNotFoundException;
import com.usco.sgci.repository.RolRepository;
import com.usco.sgci.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findByDeletedAtIsNullOrderByIdAsc()
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

        Rol rol = buscarRol(request.rolId());

        Usuario usuario = new Usuario();
        usuario.setIdentificacion(limpiar(request.identificacion()));
        usuario.setNombre(limpiar(request.nombre()));
        usuario.setCorreo(limpiar(request.correo()).toLowerCase());
        usuario.setNombreUsuario(limpiar(request.nombreUsuario()));
        usuario.setClave(passwordEncoder.encode(request.clave()));
        usuario.setRol(rol);

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarUsuario(id);
        validarUnicosActualizar(id, request);
        Rol rol = buscarRol(request.rolId());

        usuario.setIdentificacion(limpiar(request.identificacion()));
        usuario.setNombre(limpiar(request.nombre()));
        usuario.setCorreo(limpiar(request.correo()).toLowerCase());
        usuario.setNombreUsuario(limpiar(request.nombreUsuario()));
        usuario.setRol(rol);

        if (request.clave() != null && !request.clave().isBlank()) {
            usuario.setClave(passwordEncoder.encode(request.clave()));
        }

        return toResponse(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuario.setDeletedAt(LocalDateTime.now(ZoneOffset.UTC));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
    }

    private Rol buscarRol(Long id) {
        return rolRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado."));
    }

    private void validarUnicosCrear(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByIdentificacionAndDeletedAtIsNull(limpiar(request.identificacion()))) {
            throw new BusinessException("La identificacion ya existe.");
        }
        if (usuarioRepository.existsByCorreoAndDeletedAtIsNull(limpiar(request.correo()).toLowerCase())) {
            throw new BusinessException("El correo ya existe.");
        }
        if (usuarioRepository.existsByNombreUsuarioAndDeletedAtIsNull(limpiar(request.nombreUsuario()))) {
            throw new BusinessException("El nombre de usuario ya existe.");
        }
    }

    private void validarUnicosActualizar(Long id, UsuarioUpdateRequest request) {
        if (usuarioRepository.existsByIdentificacionAndIdNotAndDeletedAtIsNull(limpiar(request.identificacion()), id)) {
            throw new BusinessException("La identificacion ya existe.");
        }
        if (usuarioRepository.existsByCorreoAndIdNotAndDeletedAtIsNull(limpiar(request.correo()).toLowerCase(), id)) {
            throw new BusinessException("El correo ya existe.");
        }
        if (usuarioRepository.existsByNombreUsuarioAndIdNotAndDeletedAtIsNull(limpiar(request.nombreUsuario()), id)) {
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
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
