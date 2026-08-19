package com.sistemarecepcion.controller;

import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los usuarios del sistema.
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Constructor que permite inyectar el servicio de usuarios.
     * 
     * @param usuarioService servicio de usuarios.
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios registrados.
     * 
     * @return lista de usuarios.
     */
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    /**
     * Busca un usuario por su identificador.
     * 
     * @param id identificador del usuario.
     * @return usuario encontrado o respuesta 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Registra un nuevo usuario.
     * 
     * @param usuario información del usuario.
     * @return usuario registrado.
     */
    @PostMapping
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    /**
     * Actualiza un usuario existente.
     * 
     * @param id identificador del usuario.
     * @param usuario información actualizada del usuario.
     * @return usuario actualizado o respuesta 404 si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
        @PathVariable Integer id,
        @RequestBody Usuario usuario) {

            return usuarioService.buscarPorId(id)
            .map(usuarioExistente -> {
                usuario.setIdUsuario(id);
                return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
        }

    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id identificador del usuario.
     * @return respuesta indicando si la operación fue realizada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {

        if (usuarioService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}