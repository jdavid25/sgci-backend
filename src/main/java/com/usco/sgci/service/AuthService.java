package com.usco.sgci.service;

import com.usco.sgci.dto.auth.LoginRequest;
import com.usco.sgci.dto.auth.LoginResponse;
import com.usco.sgci.entity.Usuario;
import com.usco.sgci.exception.UnauthorizedException;
import com.usco.sgci.repository.UsuarioRepository;
import com.usco.sgci.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByNombreUsuarioAndDeletedAtIsNull(request.nombreUsuario().trim())
                .orElseThrow(() -> new UnauthorizedException("Usuario o clave invalidos."));

        if (!passwordEncoder.matches(request.clave(), usuario.getClave())) {
            throw new UnauthorizedException("Usuario o clave invalidos.");
        }

        return new LoginResponse(
                jwtService.generarToken(usuario),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getRol().getNombre()
        );
    }
}
