package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    public String generarContrasenaCifrada(String contrasena) {
        return passwordEncoder.encode(contrasena);
    }

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder =
    new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorCedula(String cedula) {
        return usuarioRepository.findByUsuaCedula(cedula);
    }

    public boolean verificarContrasena(String contrasenaIngresada,
        String contrasenaCifrada) {

            if (contrasenaIngresada == null || contrasenaCifrada == null) {
                return false;
            }

            return passwordEncoder.matches(
                contrasenaIngresada,
                contrasenaCifrada
            );
        }

    // Registrar un usuario nuevo
    public Usuario guardarUsuario(Usuario usuario) {

        if (usuario.getUsuaContrasena() != null
    && !usuario.getUsuaContrasena().isBlank()) {

        usuario.setUsuaContrasena(
            passwordEncoder.encode(usuario.getUsuaContrasena())
        );
    }

    return usuarioRepository.save(usuario);
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(Usuario usuarioExistente,
        Usuario datosNuevos) {

            usuarioExistente.setUsuaNombre(datosNuevos.getUsuaNombre());
            usuarioExistente.setUsuaCedula(datosNuevos.getUsuaCedula());
            usuarioExistente.setUsuaTelefono(datosNuevos.getUsuaTelefono());
            usuarioExistente.setUsuaCorreo(datosNuevos.getUsuaCorreo());
            usuarioExistente.setUsuaApartamento(datosNuevos.getUsuaApartamento());
            usuarioExistente.setIdRol(datosNuevos.getIdRol());

            if (datosNuevos.getUsuaContrasena() != null
        && !datosNuevos.getUsuaContrasena().isBlank()) {

            usuarioExistente.setUsuaContrasena(
                passwordEncoder.encode(datosNuevos.getUsuaContrasena())
            );
        }

        return usuarioRepository.save(usuarioExistente);
        }

        public void eliminarUsuario(Integer id) {
            usuarioRepository.deleteById(id);
        }
}