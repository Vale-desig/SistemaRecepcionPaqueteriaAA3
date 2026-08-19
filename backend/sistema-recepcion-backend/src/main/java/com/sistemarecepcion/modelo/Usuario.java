package com.sistemarecepcion.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa la tabla usuarios de la base de datos.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "IdUsuario")
   private Integer idUsuario;

   @Column(name = "UsuaNombre")
   private String usuaNombre;

   @Column(name = "UsuaCedula")
   private String usuaCedula;

   @Column(name = "UsuaTelefono")
   private String usuaTelefono;

   @Column(name = "UsuaCorreo")
   private String usuaCorreo;

   @Column(name = "UsuaApartamento")
   private String usuaApartamento;

   @Column(name = "IdRol")
   private Integer idRol;

   public Usuario() {
   }

   public Integer getIdUsuario() {
       return idUsuario;
   }

   public void setIdUsuario(Integer idUsuario) {
       this.idUsuario = idUsuario;
   }

   public String getUsuaNombre() {
       return usuaNombre;
   }

   public void setUsuaNombre(String usuaNombre) {
       this.usuaNombre = usuaNombre;
   }

   public String getUsuaCedula() {
       return usuaCedula;
   }

   public void setUsuaCedula(String usuaCedula) {
       this.usuaCedula = usuaCedula;
   }

   public String getUsuaTelefono() {
       return usuaTelefono;
   }

   public void setUsuaTelefono(String usuaTelefono) {
       this.usuaTelefono = usuaTelefono;
   }

   public String getUsuaCorreo() {
       return usuaCorreo;
   }

   public void setUsuaCorreo(String usuaCorreo) {
       this.usuaCorreo = usuaCorreo;
   }

   public String getUsuaApartamento() {
       return usuaApartamento;
   }

   public void setUsuaApartamento(String usuaApartamento) {
       this.usuaApartamento = usuaApartamento;
   }

   public Integer getIdRol() {
       return idRol;
   }

   public void setIdRol(Integer idRol) {
       this.idRol = idRol;
   }
}