package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Entrega;
import com.sistemarecepcion.modelo.Turno;
import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.repository.EntregaRepository;
import com.sistemarecepcion.repository.PaqueteRepository;
import com.sistemarecepcion.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final PaqueteRepository paqueteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoService turnoService;

    public EntregaService(
            EntregaRepository entregaRepository,
            PaqueteRepository paqueteRepository,
            UsuarioRepository usuarioRepository,
            TurnoService turnoService){
        
        this.entregaRepository = entregaRepository;
        this.paqueteRepository = paqueteRepository;
        this.usuarioRepository = usuarioRepository;
        this.turnoService = turnoService;
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
        
        Usuario portero = usuarioRepository.findById(
                entrega.getIdPortero())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el portero."));
        
        LocalTime horaEntrega = entrega.getEntrHoraEntrega();
        
        if (horaEntrega == null){
            horaEntrega = LocalTime.now();
            entrega.setEntrHoraEntrega(horaEntrega);
        }
        
        Turno turno = turnoService.buscarTurnoActual(horaEntrega);
        
        entrega.setEntrPorteroNombre(portero.getUsuaNombre());
        entrega.setEntrTurno(turno.getTurnoNombre());
        entrega.setEntrConfirmacion("Entregado");

        paquete.setPaquEstado("entregado");
        paqueteRepository.save(paquete);

        return entregaRepository.save(entrega);
    }
}