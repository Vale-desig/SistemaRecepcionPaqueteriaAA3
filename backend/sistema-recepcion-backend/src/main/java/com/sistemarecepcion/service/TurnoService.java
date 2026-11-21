package com.sistemarecepcion.service;

import com.sistemarecepcion.modelo.Turno;
import com.sistemarecepcion.repository.TurnoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class TurnoService {
    
    private final TurnoRepository turnoRepository;
    
    public TurnoService(TurnoRepository turnoRepository){
        this.turnoRepository = turnoRepository;
    }
    
    public List<Turno> listarTurnos(){
        return turnoRepository.findAll();
    }
    
    public List<Turno> listarTurnosActivos(){
        return turnoRepository.findByTurnoActivoTrue();
    }
    
    public Turno buscarTurnoActual(LocalTime hora){
        
        List<Turno> turnos = listarTurnosActivos();
        
        for (Turno turno : turnos){
            
            LocalTime inicio =turno.getTurnoHoraInicio();
            LocalTime fin = turno.getTurnoHoraFin();
            
            boolean dentroDelTurno;
            
            if (inicio.isBefore(fin)){
                dentroDelTurno = !hora.isBefore(inicio)
                        && hora.isBefore(fin);
            } else{
                dentroDelTurno = !hora.isBefore(inicio)
                        || hora.isBefore(fin);
            }
            
            if (dentroDelTurno){
                return turno;
            }
        }
        
        throw new IllegalStateException(
                "No hay un turno activo configurado para esta hora."
        );
    }
    
    @Transactional
    public Turno actualizarTurno(Integer id, Turno datos){
        
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El turno no existe."
                ));
        
        if (datos.getTurnoNombre() == null
                || datos.getTurnoNombre().isBlank()
                || datos.getTurnoHoraInicio() == null
                || datos.getTurnoHoraFin() == null){
            
            throw new IllegalArgumentException(
                    "Debe completar el nombre y los horarios del turno."
            );
        }
        
        turno.setTurnoNombre(datos.getTurnoNombre());
        turno.setTurnoHoraInicio(datos.getTurnoHoraInicio());
        turno.setTurnoHoraFin(datos.getTurnoHoraFin());
        
        if (datos.getTurnoActivo() != null){
            turno.setTurnoActivo(datos.getTurnoActivo());
        }
        
        return turnoRepository.save(turno);
    }
}
