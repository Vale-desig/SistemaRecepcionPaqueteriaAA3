package com.sistemarecepcion.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity 
@Table(name = "paquetes")
public class Paquete {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPaquete")
    private Integer idPaquete;

    @Column(name = "PaquFechaLlegada", nullable = false)
    private LocalDate paquFechaLlegada;

    @Column(name = "PaquDescripcion", nullable = false)
    private String paquDescripcion;

    @Column(name = "PaquRemitente", nullable = false)
    private String paquRemitente;

    @Column(name = "PaquEstado", nullable = false)
    private String paquEstado;

    @Column(name = "PaquPeso")
    private BigDecimal paquPeso;

    @Column(name = "PaquDanio")
    private String paquDanio;

    @Column(name = "IdUsuario", nullable = false)
    private Integer idUsuario;
    
    @Column(name = "PaquHoraLlegada")
    private LocalTime paquHoraLlegada;
    
    @Column(name = "IdPorteroRecepcion")
    private Integer idPorteroRecepcion;
    
    @Column(name = "PaquPorteroRecepcion")
    private String paquPorteroRecepcion;
    
    @Column(name = "PaquTurnoRecepcion")
    private String paquTurnoRecepcion;

    public Paquete() {
    }

    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public LocalDate getPaquFechaLlegada() {
        return paquFechaLlegada;
    }

    public void setPaquFechaLlegada(LocalDate paquFechaLlegada) {
        this.paquFechaLlegada = paquFechaLlegada;
    }

    public String getPaquDescripcion() {
        return paquDescripcion;
    }

    public void setPaquDescripcion(String paquDescripcion) {
        this.paquDescripcion = paquDescripcion;
    }

    public String getPaquRemitente() {
        return paquRemitente;
    }

    public void setPaquRemitente(String paquRemitente) {
        this.paquRemitente = paquRemitente;
    }

    public String getPaquEstado() {
        return paquEstado;
    }

    public void setPaquEstado(String paquEstado) {
        this.paquEstado = paquEstado;
    }

    public BigDecimal getPaquPeso() {
        return paquPeso;
    }

    public void setPaquPeso(BigDecimal paquPeso) {
        this.paquPeso = paquPeso;
    }

    public String getPaquDanio() {
        return paquDanio;
    }

    public void setPaquDanio(String paquDanio) {
        this.paquDanio = paquDanio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public LocalTime getPaquHoraLlegada(){
        return paquHoraLlegada;
    }
    
    public void setPaquHoraLlegada(LocalTime paquHoraLlegada){
        this.paquHoraLlegada = paquHoraLlegada;
    }
    
    public Integer getIdPorteroRecepcion(){
        return idPorteroRecepcion;
    }
    
    public void setIdPorteroRecepcion(Integer idPorteroRecepcion){
        this.idPorteroRecepcion = idPorteroRecepcion;
    }
    
    public String getPaquPorteroRecepcion(){
        return paquPorteroRecepcion;
    }
    
    public void setPaquPorteroRecepcion(String paquPorteroRecepcion){
        this.paquPorteroRecepcion = paquPorteroRecepcion;
    }
    
    public String getPaquTurnoRecepcion(){
        return paquTurnoRecepcion;
    }
    
    public void setPaquTurnoRecepcion(String paquTurnoRecepcion){
        this.paquTurnoRecepcion = paquTurnoRecepcion;
    }
}