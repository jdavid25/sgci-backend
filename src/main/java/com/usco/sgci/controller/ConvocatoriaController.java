package com.usco.sgci.controller;

import com.usco.sgci.dto.convocatoria.ConvocatoriaCreateRequest;
import com.usco.sgci.dto.convocatoria.ConvocatoriaResponse;
import com.usco.sgci.dto.convocatoria.ConvocatoriaUpdateRequest;
import com.usco.sgci.service.ConvocatoriaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/convocatorias")
public class ConvocatoriaController {

    private final ConvocatoriaService convocatoriaService;

    @GetMapping
    public List<ConvocatoriaResponse> listar() {
        return convocatoriaService.listar();
    }

    @GetMapping("/{id}")
    public ConvocatoriaResponse obtenerPorId(@PathVariable Long id) {
        return convocatoriaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConvocatoriaResponse crear(@Valid @RequestBody ConvocatoriaCreateRequest request) {
        return convocatoriaService.crear(request);
    }

    @PutMapping("/{id}")
    public ConvocatoriaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConvocatoriaUpdateRequest request
    ) {
        return convocatoriaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        convocatoriaService.eliminar(id);
    }
}
