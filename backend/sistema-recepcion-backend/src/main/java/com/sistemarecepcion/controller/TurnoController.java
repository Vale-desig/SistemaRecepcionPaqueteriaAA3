package com.sistemarecepcion.controller;

import com.sistemarecepcion.modelo.Turno;
import com.sistemarecepcion.service.TurnoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "http://localhost:5173")
public class TurnoController {
    
    private final TurnoService turnoService;
    
    public TurnoController(TurnoService turnoService){
        this.turnoService = turnoService;
    }
    
    @GetMapping
    public List<Turno> listarTurnos(){
        return turnoService.listarTurnos();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTurno(
            @PathVariable Integer id,
            @RequestBody Turno turno){
        
        try {
            return ResponseEntity.ok(
                   turnoService.actualizarTurno(id, turno)
           );
            
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}
