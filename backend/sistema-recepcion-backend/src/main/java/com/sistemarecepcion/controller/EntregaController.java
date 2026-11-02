package com.sistemarecepcion.controller;

import com.sistemarecepcion.modelo.Entrega;
import com.sistemarecepcion.service.EntregaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController 
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "http://localhost:5173")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @GetMapping 
    public List<Entrega> listarEntregas() {
        return entregaService.listarEntregas();
    }

    @PostMapping 
    public ResponseEntity<?> registrarEntrega(
        @RequestBody Entrega entrega,
        Authentication authentication) {

            try {
                Integer idPortero = Integer.valueOf(authentication.getName());

                entrega.setIdEntrega(null);
                entrega.setIdPortero(idPortero);
                entrega.setEntrFechaEntrega(LocalDate.now());
                entrega.setEntrHoraEntrega(LocalTime.now());

                Entrega nuevaEntrega = entregaService.registrarEntrega(entrega);

                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEntrega);

            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                .body(Map.of("mensaje", e.getMessage()));
            }
        }
    
}
