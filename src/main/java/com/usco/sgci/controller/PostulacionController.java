package com.usco.sgci.controller;

import com.usco.sgci.dto.postulacion.PostulacionCreateRequest;
import com.usco.sgci.dto.postulacion.PostulacionEstadoRequest;
import com.usco.sgci.dto.postulacion.PostulacionResponse;
import com.usco.sgci.service.PostulacionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    private final PostulacionService postulacionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostulacionResponse crear(@Valid @RequestBody PostulacionCreateRequest request) {
        return postulacionService.crear(request);
    }

    @GetMapping
    public List<PostulacionResponse> listar() {
        return postulacionService.listar();
    }

    @GetMapping("/mis-postulaciones")
    public List<PostulacionResponse> listarMisPostulaciones() {
        return postulacionService.listarMisPostulaciones();
    }

    @PutMapping("/{id}/estado")
    public PostulacionResponse cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody PostulacionEstadoRequest request
    ) {
        return postulacionService.cambiarEstado(id, request);
    }
}
