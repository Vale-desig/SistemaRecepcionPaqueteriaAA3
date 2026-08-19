package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Contiene la lógica de negocio relacionada con los usuarios.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor que permite inyectar el repositorio de usuarios.
     * 
     * @param usuarioRepository repositorio de usuarios.
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los usuarios registrados.
     * 
     * @return lista de usuarios.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su identificador.
     * 
     * @param id identificador del usuario.
     * @return usuario encontrado, si existe.
     */
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Guarda un nuevo usuario o actualiza uno  existente.
     * 
     * @param usuario usuario que se desea guardar.
     * @return usuario guardado.
     */
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id identificador del usuario.
     */
    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}