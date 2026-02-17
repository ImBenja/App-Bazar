package com.bazar.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.bazar.app.enums.EstadoProducto;
import com.bazar.app.enums.EstadoVenta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "venta")
public class Venta {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "codigo_venta")
  Long codigoVenta;
  @Column(name = "fecha_venta")
  LocalDate fecha;
  @Column(name = "total")
  BigDecimal total;
  @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  List<DetalleVenta> detalles;
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "id_cliente")
  Cliente cliente;
  @Column(name = "estado")
  EstadoVenta estado;
  @Column(name = "fecha_creacion")
  LocalDateTime createdAt;
  @Column(name = "fecha_actualizacion")
  LocalDateTime updateAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updateAt = LocalDateTime.now();
    estado = EstadoVenta.PENDIENTE;
  }

  @PreUpdate
  protected void onUpdate() {
    updateAt = LocalDateTime.now();
  }

  public BigDecimal calcularTotal() {
    if (detalles == null || detalles.isEmpty()) {
      return BigDecimal.ZERO;
    }

    return detalles.stream()
        .map(DetalleVenta::getSubtotal)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Boolean validarStock() {
    if (detalles == null || detalles.isEmpty()) {
      throw new IllegalStateException("La venta no tiene detalles");
    }
    for (DetalleVenta dV : detalles) {
      Producto producto = dV.getProducto();
      if (producto == null) {
        return false;
      }
      Integer cantidadDisponible = dV.getCantidad();
      Integer stockDisponible = producto.getStock();

      if (cantidadDisponible > stockDisponible) {
        return false;
      }

    }
    return true;
  }

  public void descontarStock() {

    if (!validarStock()) {
      throw new IllegalArgumentException("No hay suficiente stock para uno o más productos de la venta");
    }

    for (DetalleVenta dV : detalles) {
      Producto producto = dV.getProducto();
      producto.setStock(producto.getStock() - dV.getCantidad());
    }
  }

  public void restaurarStock() {

    for (DetalleVenta dV : detalles) {
      Producto producto = dV.getProducto();
      producto.setStock(producto.getStock() + dV.getCantidad());
    }
  }

  // Despues de que se crea una venta y si el producto se queda sin stock, el
  // estado del producto se cambia a AGOTADO
  public void validarEstadoProductos() {
    for (DetalleVenta dV : detalles) {
      Producto producto = dV.getProducto();
      if (producto.getStock() - dV.getCantidad() <= 0) {
        producto.setEstado(EstadoProducto.AGOTADO);
      }
    }
  }

  public void cambiarEstado(EstadoVenta nuevoEstado) {
    this.estado = nuevoEstado;
  }
}
