package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para realizar operaciones sobre la tabla usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    
}