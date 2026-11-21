package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    
    List<Turno> findByTurnoActivoTrue();
}
