package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.RecuperacionContrasena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecuperacionContrasenaRepository 
    extends JpaRepository<RecuperacionContrasena, Integer> {

        List<RecuperacionContrasena> findByCedulaUsuarioAndUsadoFalse(
            String cedulaUsuario);

        Optional<RecuperacionContrasena> findByCedulaUsuarioAndCodigoHash(
            String cedulaUsuario, String codigoHash);
}

