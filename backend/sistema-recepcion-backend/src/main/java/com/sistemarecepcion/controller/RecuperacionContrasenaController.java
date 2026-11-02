package com.sistemarecepcion.controller;

import com.sistemarecepcion.service.RecuperacionContrasenaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController 
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class RecuperacionContrasenaController {

    private final RecuperacionContrasenaService recuperacionService;

    public RecuperacionContrasenaController(
        RecuperacionContrasenaService recuperacionService) {
            this.recuperacionService = recuperacionService;
        }

        // Solicitar código de recuperación
        @PostMapping("/recuperar")
        public ResponseEntity<?> solicitarCodigo(
            @RequestBody Map<String, String> datos) {

                String cedula = datos.get("cedula");

                if (cedula == null || cedula.isBlank()) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Debe ingresar su cédula."));
                }

                recuperacionService.solicitarCodigo(cedula);

                return ResponseEntity.ok(Map.of(
                    "mensaje",
                    "Si la cuenta puede recuperar su contraseña, recibirá un código en su correo registrado."
                ));
            }

            // Verificar código recibido
            @PostMapping("/verificar-codigo")
            public ResponseEntity<?> verificarCodigo(
                @RequestBody Map<String, String> datos) {

                    String cedula = datos.get("cedula");
                    String codigo = datos.get("codigo");

                    if (cedula == null || cedula.isBlank()
                    || codigo == null || codigo.isBlank()) {
                return ResponseEntity.badRequest().body(
                    Map.of("mensaje", "Debe ingresar la cédula y el código."));
                }

                boolean valido = recuperacionService.verificarCodigo(
                    cedula, codigo);

                    if (!valido) {
                        return ResponseEntity.badRequest().body(
                            Map.of("mensaje", "El código es incorrecto o ha expirado."));
                    }

                    return ResponseEntity.ok(Map.of(
                        "mensaje", "Código verificado correctamente."
                    ));
                }

                // Cambiar contraseña
                @PostMapping("/cambiar-contrasena")
                public ResponseEntity<?> cambiarContrasena(
                    @RequestBody Map<String, String> datos) {

                        String cedula = datos.get("cedula");
                        String codigo = datos.get("codigo");
                        String nuevaContrasena = datos.get("nuevaContrasena");

                        if (cedula == null || cedula.isBlank()
                        || codigo == null || codigo.isBlank()
                    || nuevaContrasena == null || nuevaContrasena.isBlank()) {
                        return ResponseEntity.badRequest().body(
                            Map.of("mensaje", "Debe completar todos los campos."));
                    }

                    boolean cambiada = recuperacionService.cambiarContrasena(
                        cedula, codigo, nuevaContrasena);

                        if (!cambiada) {
                            return ResponseEntity.badRequest().body(
                                Map.of("mensaje",
                                    "No se pudo cambiar la contraseña. Verifique el código y los datos ingresados."));
                        }

                        return ResponseEntity.ok(Map.of(
                            "mensaje", "La contraseña se cambió correctamente."
                        ));
                    }
    
}
