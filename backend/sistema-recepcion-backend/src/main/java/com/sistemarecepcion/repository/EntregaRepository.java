package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntregaRepository extends JpaRepository<Entrega, Integer> {

    Optional<Entrega> findByIdPaquete(Integer idPaquete);

    
}
