package com.usco.sgci.controller;

import com.usco.sgci.dto.estado.EstadoResponse;
import com.usco.sgci.service.EstadoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    public List<EstadoResponse> listarPorTipo(@RequestParam(required = false) String tipo) {
        return estadoService.listarPorTipo(tipo);
    }
}
