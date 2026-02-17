package com.bazar.app.model;

import java.time.LocalDateTime;

import com.bazar.app.enums.EstadoCliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "cliente")
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  Long id;
  @Column(name = "nombre")
  String nombre;
  @Column(name = "apellido")
  String apellido;
  @Column(name = "dni", unique = true)
  String dni;
  @Column(name = "estado")
  EstadoCliente estado;
  @Column(name = "fecha_creacion")
  LocalDateTime createdAt;
  @Column(name = "fecha_actualizacion")
  LocalDateTime updateAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updateAt = LocalDateTime.now();
    estado = EstadoCliente.ACTIVO;
  }

  @PreUpdate
  protected void onUpdate() {
    updateAt = LocalDateTime.now();
  }

  public void activar() {
    this.estado = EstadoCliente.ACTIVO;
  }

  public void inactivar() {
    this.estado = EstadoCliente.INACTIVO;
  }

  public void bloquear() {
    this.estado = EstadoCliente.BLOQUEADO;
  }
}
