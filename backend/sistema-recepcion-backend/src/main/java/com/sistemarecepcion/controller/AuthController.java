package com.sistemarecepcion.controller;

import com.sistemarecepcion.JwtService;
import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController 
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(
        UsuarioService usuarioService,
        JwtService jwtService) {
            this.usuarioService = usuarioService;
            this.jwtService = jwtService;
        }

        @PostMapping("/login")
        public ResponseEntity<?> iniciarSesion(
            @RequestBody Map<String, String> datos) {

                String correo = datos.get("correo");
                String cedula = datos.get("cedula");
                String contrasena = datos.get("contrasena");

                if (((correo == null || correo.isBlank())
                && (cedula == null || cedula.isBlank()))
                || contrasena == null || contrasena.isBlank()) {

                    return ResponseEntity.badRequest()
                    .body(Map.of(
                        "mensaje",
                        "Debe ingresar el correo o la cédula y la contraseña."
                    ));
                }

                Usuario usuario;

                if (correo != null && !correo.isBlank()) {
                    usuario = usuarioService.buscarPorCorreo(correo.trim())
                            .orElse(null);
                } else {
                    usuario = usuarioService.buscarPorCedula(cedula.trim())
                            .orElse(null);
                }

                if (usuario == null
                    || usuario.getUsuaContrasena() == null
                    || !usuarioService.verificarContrasena(
                        contrasena, 
                        usuario.getUsuaContrasena())) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                            "mensaje",
                            "Correo o contraseña incorrectos."
                        ));
                }

                String token = jwtService.generarToken(
                    usuario.getIdUsuario(), 
                    usuario.getIdRol()
                );

                return ResponseEntity.ok(Map.of(
                    "mensaje", "Inicio de sesión correcto.",
                    "idUsuario", usuario.getIdUsuario(),
                    "nombre", usuario.getUsuaNombre(),
                    "idRol", usuario.getIdRol(),
                    "token", token
                ));
    }
}