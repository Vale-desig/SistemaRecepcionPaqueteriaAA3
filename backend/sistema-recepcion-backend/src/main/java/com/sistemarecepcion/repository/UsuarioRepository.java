package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para realizar operaciones sobre la tabla usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsuaCedula(String usuaCedula);
    
}