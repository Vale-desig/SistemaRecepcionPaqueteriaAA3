
package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Entrega;
import com.sistemarecepcion.repository.EntregaRepository;
import com.sistemarecepcion.repository.PaqueteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final PaqueteRepository paqueteRepository;

    public EntregaService(
            EntregaRepository entregaRepository,
            PaqueteRepository paqueteRepository) {
        this.entregaRepository = entregaRepository;
        this.paqueteRepository = paqueteRepository;
    }

    public List<Entrega> listarEntregas() {
        return entregaRepository.findAll();
    }

    public Optional<Entrega> buscarPorId(Integer id) {
        return entregaRepository.findById(id);
    }

    public Optional<Entrega> buscarPorPaquete(Integer idPaquete) {
        return entregaRepository.findByIdPaquete(idPaquete);
    }

    @Transactional
    public Entrega registrarEntrega(Entrega entrega) {

        if (entrega.getIdPaquete() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un paquete.");
        }

        if (entrega.getIdPortero() == null) {
            throw new IllegalArgumentException(
                    "No se ha identificado al portero.");
        }

        if (entregaRepository.findByIdPaquete(
                entrega.getIdPaquete()).isPresent()) {
            throw new IllegalArgumentException(
                    "Este paquete ya tiene una entrega registrada.");
        }

        var paquete = paqueteRepository.findById(
                entrega.getIdPaquete())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El paquete no está registrado."));

        if (!"recibido".equalsIgnoreCase(paquete.getPaquEstado())) {
            throw new IllegalArgumentException(
                    "El paquete no está disponible para entrega.");
        }

        paquete.setPaquEstado("entregado");
        paqueteRepository.save(paquete);

        entrega.setEntrConfirmacion("Entregado");

        return entregaRepository.save(entrega);
    }
}