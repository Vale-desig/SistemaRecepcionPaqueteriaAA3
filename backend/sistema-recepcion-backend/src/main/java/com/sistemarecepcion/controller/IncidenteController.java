
package com.sistemarecepcion.controller;

import com.sistemarecepcion.modelo.Incidente;
import com.sistemarecepcion.service.IncidenteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidentes")
@CrossOrigin(origins = "http://localhost:5173")
public class IncidenteController {

    private final IncidenteService incidenteService;

    public IncidenteController(IncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(
            @RequestBody Incidente incidente,
            Authentication authentication) {

        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "Debe iniciar sesión."));
            }

            Integer idPortero = Integer.parseInt(authentication.getName());

            incidente.setIdPortero(idPortero);

            Incidente nuevo = incidenteService.registrar(incidente);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Incidente>> listarTodos() {
        return ResponseEntity.ok(incidenteService.listarTodos());
    }
}