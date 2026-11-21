
package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Incidente;
import com.sistemarecepcion.modelo.Paquete;
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

        if (incidente.getIdPaquete() == null) {
            throw new RuntimeException(
                "Debe seleccionar un paquete."
            );
        }

        Paquete paquete = paqueteRepository
        .findById(incidente.getIdPaquete())
        .orElseThrow(() -> new RuntimeException(
            "El paquete seleccionado no existe."
        ));

        if (!"recibido".equalsIgnoreCase(paquete.getPaquEstado())) {
            throw new RuntimeException(
                "No se pueden registrar incidentes en paquetes que ya fueron entregados."
            );
        }

        incidente.setInciFecha(LocalDate.now());
        incidente.setInciHora(LocalTime.now().withNano(0));

        return incidenteRepository.save(incidente);
    }

    public List<Incidente> listarTodos() {
        return incidenteRepository.findAll();
    }

    public List<Incidente> listarPorResidente(Integer idUsuario) {

        List<Paquete> paquetes =
        paqueteRepository.findByIdUsuario(idUsuario);

        List<Integer> idsPaquetes = paquetes.stream()
        .map(Paquete::getIdPaquete)
        .toList();

        if (idsPaquetes.isEmpty()) {
            return List.of();
        }

        return incidenteRepository.findByIdPaqueteIn(idsPaquetes);
    }
}