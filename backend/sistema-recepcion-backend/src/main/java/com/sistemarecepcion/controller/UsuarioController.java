package com.sistemarecepcion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *Controlador REST del módulo de usuarios.
 *
 *Por ahora contiene un endpoint de prueba para comprobar
 *que el controlador está funcionando correctamente.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    /**
     * Endpoint de prueba del módulo de usuarios.
     *
     * @return mensaje indicando que el módulo está funcionando.
     */
    @GetMapping("/prueba")
    public String prueba() {
        return "Módulo de usuarios funcionando correctamente";
    }
}