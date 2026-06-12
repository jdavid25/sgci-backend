package com.usco.sgci.repository;

import com.usco.sgci.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByNombreUsuarioAndEstadoNombre(String nombreUsuario, String estadoNombre);

    Optional<Usuario> findByIdAndEstadoNombre(Long id, String estadoNombre);

    List<Usuario> findByEstadoNombreOrderByIdAsc(String estadoNombre);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByCorreo(String correo);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByIdentificacionAndIdNot(String identificacion, Long id);

    boolean existsByCorreoAndIdNot(String correo, Long id);

    boolean existsByNombreUsuarioAndIdNot(String nombreUsuario, Long id);
}
