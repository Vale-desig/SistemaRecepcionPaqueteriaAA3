package com.sistemarecepcion.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity 
@Table(name = "recuperacionescontrasena")
public class RecuperacionContrasena {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRecuperacion")
    private Integer idRecuperacion;

    @Column(name = "CedulaUsuario", nullable = false, length = 30)
    private String cedulaUsuario;

    @Column(name = "CodigoHash", nullable = false, length = 100)
    private String codigoHash;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaExpiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "Usado", nullable = false)
    private Boolean usado = false;

    public Integer getIdRecuperacion() {
        return idRecuperacion;
    }

    public void setIdRecuperacion(Integer idRecuperacion) {
        this.idRecuperacion = idRecuperacion;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public String getCodigoHash() {
        return codigoHash;
    }

    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }
    
}
