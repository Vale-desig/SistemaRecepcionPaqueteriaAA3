package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {

    List<Paquete> findByIdUsuario(Integer idUsuario);
    
}