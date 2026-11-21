package com.sistemarecepcion.repository;

import com.sistemarecepcion.modelo.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  java.util.List;

@Repository 
public interface IncidenteRepository extends JpaRepository<Incidente, Integer> {

    List<Incidente> findByIdPaqueteIn(List<Integer> idsPaquete);

}