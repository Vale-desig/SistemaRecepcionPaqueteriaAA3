package com.sistemarecepcion.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity 
@Table(name = "incidentes")
public class Incidente {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdIncidente")
    private Integer idIncidente;

    @Column(name = "InciTipo", nullable = false)
    private String inciTipo;

    @Column(name = "InciDescripcion", nullable = false)
    private String inciDescripcion;

    @Column(name = "InciFecha", nullable = false)
    private LocalDate inciFecha;

    @Column(name = "InciHora")
    private LocalTime inciHora;

    @Column(name = "IdPaquete", nullable = false)
    private Integer idPaquete;

    @Column(name = "IdPortero", nullable = false)
    private Integer idPortero;

    public Incidente() {
    }

    public Integer getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(Integer idIncidente) {
        this.idIncidente = idIncidente;
    }

    public String getInciTipo() {
        return inciTipo;
    }

    public void setInciTipo(String inciTipo) {
        this.inciTipo = inciTipo;
    }

    public String getInciDescripcion() {
        return inciDescripcion;
    }

    public void setInciDescripcion(String inciDescripcion) {
        this.inciDescripcion = inciDescripcion;
    }

    public LocalDate getInciFecha() {
        return inciFecha;
    }

    public void setInciFecha(LocalDate inciFecha) {
        this.inciFecha = inciFecha;
    }

    public LocalTime getInciHora() {
        return inciHora;
    }

    public void setInciHora(LocalTime inciHora) {
        this.inciHora = inciHora;
    }

    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Integer getIdPortero() {
        return idPortero;
    }

    public void setIdPortero(Integer idPortero) {
        this.idPortero = idPortero;
    }
}