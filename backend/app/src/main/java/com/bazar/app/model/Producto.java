package com.bazar.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bazar.app.enums.EstadoProducto;

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
@Table(name = "producto")
public class Producto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "codigo_producto")
  Long codigoProducto;
  @Column(name = "nombre_producto")
  String nombre;
  @Column(name = "marca")
  String marca;
  @Column(name = "categoria")
  String categoria;
  @Column(name = "precio")
  BigDecimal precio;
  @Column(name = "stock")
  Integer stock;
  @Column(name = "estado")
  EstadoProducto estado;
  @Column(name = "fecha_creacion")
  LocalDateTime createdAt;
  @Column(name = "fecha_actualizacion")
  LocalDateTime updateAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updateAt = LocalDateTime.now();
    estado = EstadoProducto.ACTIVO;
  }

  @PreUpdate
  protected void onUpdate() {
    updateAt = LocalDateTime.now();
  }

  public void activar() {
    if (this.stock != null && this.stock > 0) {
      this.estado = EstadoProducto.ACTIVO;
    } else {
      throw new IllegalStateException("No se puede activar un producto que no tiene stock disponible");
    }
  }

  public void inactivar() {
    this.estado = EstadoProducto.INACTIVO;
  }

  public void marcarAgotado() {
    if (this.stock != null && this.stock == 0) {
      this.estado = EstadoProducto.AGOTADO;
    } else {
      throw new IllegalStateException("No se puede marcar como AGOTADO un producto que tiene stock disponible");
    }
  }

  public void descontinuar() {
    this.estado = EstadoProducto.DESCONTINUADO;
  }
}
