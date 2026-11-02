package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface IncidenteRepository extends JpaRepository<Incidente, Integer> {

}