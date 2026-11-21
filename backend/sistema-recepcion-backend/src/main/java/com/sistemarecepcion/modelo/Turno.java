package com.sistemarecepcion.modelo;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "turnos")
public class Turno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTurno")
    private Integer idTurno;
    
    @Column(name = "TurnoNombre", nullable = false)
    private String turnoNombre;
    
    @Column(name = "TurnoHoraInicio", nullable = false)
    private LocalTime turnoHoraInicio;
    
    @Column(name = "TurnoHoraFin", nullable = false)
    private LocalTime turnoHoraFin;
    
    @Column(name = "TurnoActivo", nullable = false)
    private Boolean turnoActivo;
    
    public Turno(){
    }
    
    public Integer getIdTurno(){
        return idTurno;
    }
    
    public void setIdTurno(Integer idTurno){
        this.idTurno = idTurno;
    }
    
    public String getTurnoNombre(){
        return turnoNombre;
    }
    
    public void setTurnoNombre(String turnoNombre){
        this.turnoNombre = turnoNombre;
    }
    
    public LocalTime getTurnoHoraInicio(){
        return turnoHoraInicio;
    }
    
    public void setTurnoHoraInicio(LocalTime turnoHoraInicio){
        this.turnoHoraInicio = turnoHoraInicio;
    }
    
    public LocalTime getTurnoHoraFin(){
        return turnoHoraFin;
    }
    
    public void setTurnoHoraFin(LocalTime turnoHoraFin){
        this.turnoHoraFin = turnoHoraFin;
    }
    
    public Boolean getTurnoActivo(){
        return turnoActivo;
    }
    
    public void setTurnoActivo(Boolean turnoActivo){
        this.turnoActivo = turnoActivo;
    }
}