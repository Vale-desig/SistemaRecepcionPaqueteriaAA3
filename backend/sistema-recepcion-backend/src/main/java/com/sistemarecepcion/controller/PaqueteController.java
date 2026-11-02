
package com.sistemarecepcion.controller;

import com.sistemarecepcion.modelo.Paquete;
import com.sistemarecepcion.service.PaqueteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paquetes")
@CrossOrigin(origins = "http://localhost:5173")
public class PaqueteController {

    private final PaqueteService paqueteService;

    public PaqueteController(PaqueteService paqueteService) {
        this.paqueteService = paqueteService;
    }

    @GetMapping
    public List<Paquete> listarPaquetes(Authentication authentication) {

        boolean esResidente = authentication.getAuthorities().stream()
                .anyMatch(autoridad ->
                        autoridad.getAuthority().equals("ROLE_RESIDENTE"));

        if (esResidente) {
            Integer idUsuario = Integer.valueOf(authentication.getName());
            return paqueteService.listarPorUsuario(idUsuario);
        }

        return paqueteService.listarPaquetes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> buscarPorId(
            @PathVariable Integer id,
            Authentication authentication) {

        return paqueteService.buscarPorId(id)
                .filter(paquete -> {
                    boolean esResidente = authentication.getAuthorities().stream()
                            .anyMatch(autoridad ->
                                    autoridad.getAuthority().equals("ROLE_RESIDENTE"));

                    if (!esResidente) {
                        return true;
                    }

                    Integer idUsuario = Integer.valueOf(authentication.getName());

                    return paquete.getIdUsuario().equals(idUsuario);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Paquete guardarPaquete(@RequestBody Paquete paquete) {
        return paqueteService.guardarPaquete(paquete);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> actualizarPaquete(
            @PathVariable Integer id,
            @RequestBody Paquete paquete) {

        return paqueteService.buscarPorId(id)
                .map(paqueteExistente -> {
                    paquete.setIdPaquete(id);
                    return ResponseEntity.ok(
                            paqueteService.guardarPaquete(paquete)
                    );
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaquete(@PathVariable Integer id) {

        if (paqueteService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        paqueteService.eliminarPaquete(id);
        return ResponseEntity.noContent().build();
    }
}