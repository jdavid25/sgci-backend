package com.usco.sgci.controller;

import com.usco.sgci.dto.rol.RolResponse;
import com.usco.sgci.service.RolService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    public List<RolResponse> listarActivos() {
        return rolService.listarActivos();
    }
}
