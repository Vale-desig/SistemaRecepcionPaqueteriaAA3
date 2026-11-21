package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Paquete;
import com.sistemarecepcion.modelo.Turno;
import com.sistemarecepcion.modelo.Usuario;
import com.sistemarecepcion.repository.PaqueteRepository;
import com.sistemarecepcion.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoService turnoService;

    public PaqueteService(
            PaqueteRepository paqueteRepository,
            UsuarioRepository usuarioRepository,
            TurnoService turnoService) {
        
        this.paqueteRepository = paqueteRepository;
        this.usuarioRepository = usuarioRepository;
        this.turnoService = turnoService;
    }
    
    public List<Paquete> listarPaquetes(){
        return paqueteRepository.findAll();
    }
    
    public List<Paquete> listarPorUsuario(Integer idUsuario){
        return paqueteRepository.findByIdUsuario(idUsuario);
    }
    
    public Optional<Paquete> buscarPorId(Integer id){
        return paqueteRepository.findById(id);
    }
    
    @Transactional
    public Paquete registrarRecepcion(
            Paquete paquete, Integer idPortero){
        
        Usuario portero = usuarioRepository.findById(idPortero)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el portero."
                ));
        
        LocalTime horaRecepcion = LocalTime.now();
        
        Turno turno = turnoService.buscarTurnoActual(horaRecepcion);
        
        paquete.setIdPaquete(null);
        paquete.setIdPorteroRecepcion(idPortero);
        paquete.setPaquHoraLlegada(horaRecepcion);
        paquete.setPaquPorteroRecepcion(portero.getUsuaNombre());
        paquete.setPaquTurnoRecepcion(turno.getTurnoNombre());
        
        return paqueteRepository.save(paquete);
    }
    
    public Paquete guardarPaquete(Paquete paquete){
        return paqueteRepository.save(paquete);
    }
    
    public void eliminarPaquete(Integer id){
        paqueteRepository.deleteById(id);
    }
}