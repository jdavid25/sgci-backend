package com.usco.sgci.repository;

import com.usco.sgci.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuarioAndDeletedAtIsNull(String nombreUsuario);

    Optional<Usuario> findByIdAndDeletedAtIsNull(Long id);

    List<Usuario> findByDeletedAtIsNullOrderByIdAsc();

    boolean existsByIdentificacionAndDeletedAtIsNull(String identificacion);

    boolean existsByCorreoAndDeletedAtIsNull(String correo);

    boolean existsByNombreUsuarioAndDeletedAtIsNull(String nombreUsuario);

    boolean existsByIdentificacionAndIdNotAndDeletedAtIsNull(String identificacion, Long id);

    boolean existsByCorreoAndIdNotAndDeletedAtIsNull(String correo, Long id);

    boolean existsByNombreUsuarioAndIdNotAndDeletedAtIsNull(String nombreUsuario, Long id);
}
