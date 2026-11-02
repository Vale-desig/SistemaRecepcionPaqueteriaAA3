package com.sistemarecepcion.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity 
@Table(name = "entregas")
public class Entrega {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEntrega")
    private Integer idEntrega;

    @Column(name = "EntrFechaEntrega", nullable = false)
    private LocalDate entrFechaEntrega;

    @Column(name = "EntrHoraEntrega", nullable = false)
    private LocalTime entrHoraEntrega;

    @Column(name = "IdPaquete", nullable = false)
    private Integer idPaquete;

    @Column(name = "IdPortero", nullable = false)
    private Integer idPortero;

    @Column(name = "EntrConfirmacion", nullable = false)
    private String entrConfirmacion;

    public Integer getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public LocalDate getEntrFechaEntrega() {
        return entrFechaEntrega;
    }

    public void setEntrFechaEntrega(LocalDate entrFechaEntrega) {
        this.entrFechaEntrega = entrFechaEntrega;
    }

    public LocalTime getEntrHoraEntrega() {
        return entrHoraEntrega;
    }

    public void setEntrHoraEntrega(LocalTime entrHoraEntrega) {
        this.entrHoraEntrega = entrHoraEntrega;
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

    public String getEntrConfirmacion() {
        return entrConfirmacion;
    }

    public void setEntrConfirmacion(String entrConfirmacion) {
        this.entrConfirmacion = entrConfirmacion;
    }
}
