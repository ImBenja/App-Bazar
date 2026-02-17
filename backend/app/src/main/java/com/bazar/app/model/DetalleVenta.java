package com.bazar.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "detalle_venta")
public class DetalleVenta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  Long id;

  @Column(name = "cantidad", nullable = false)
  Integer cantidad;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codigo_producto")
  Producto producto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codigo_venta")
  Venta venta;

  @Column(name = "producto_nombre", nullable = false)
  String productoNombre;

  @Column(name = "precio_unitario", nullable = false)
  BigDecimal precioUnitario;

  @Column(name = "subtotal", nullable = false)
  BigDecimal subtotal;

  @Column(name = "fecha_creacion")
  LocalDateTime createdAt;

  @Column(name = "fecha_actualizacion")
  LocalDateTime updateAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updateAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updateAt = LocalDateTime.now();
  }

  public BigDecimal calcularSubtotal() {
    if (precioUnitario == null || cantidad == null) {
      return BigDecimal.ZERO;
    }
    return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
  }
}
