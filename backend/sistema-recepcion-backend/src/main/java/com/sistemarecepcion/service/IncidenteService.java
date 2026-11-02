
package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Incidente;
import com.sistemarecepcion.repository.IncidenteRepository;
import com.sistemarecepcion.repository.PaqueteRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;
    private final PaqueteRepository paqueteRepository;

    public IncidenteService(
            IncidenteRepository incidenteRepository,
            PaqueteRepository paqueteRepository) {
        this.incidenteRepository = incidenteRepository;
        this.paqueteRepository = paqueteRepository;
    }

    public Incidente registrar(Incidente incidente) {

        if (incidente.getInciTipo() == null ||
            (!incidente.getInciTipo().equalsIgnoreCase("Dañado") &&
             !incidente.getInciTipo().equalsIgnoreCase("Perdido"))) {
            throw new RuntimeException(
                "El tipo debe ser Dañado o Perdido."
            );
        }

        if (incidente.getInciDescripcion() == null ||
            incidente.getInciDescripcion().isBlank()) {
            throw new RuntimeException(
                "Debe ingresar una descripción del incidente."
            );
        }

        if (incidente.getIdPaquete() == null ||
            !paqueteRepository.existsById(incidente.getIdPaquete())) {
            throw new RuntimeException(
                "El paquete seleccionado no existe."
            );
        }

        incidente.setInciFecha(LocalDate.now());
        incidente.setInciHora(LocalTime.now().withNano(0));

        return incidenteRepository.save(incidente);
    }

    public List<Incidente> listarTodos() {
        return incidenteRepository.findAll();
    }
}