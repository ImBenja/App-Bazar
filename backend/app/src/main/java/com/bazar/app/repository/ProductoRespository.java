package com.bazar.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazar.app.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import com.bazar.app.enums.EstadoProducto;

@Repository
public interface ProductoRespository extends JpaRepository<Producto, Long> {
  List<Producto> findByCategoriaContainingIgnoreCase(String categoria);

  List<Producto> findByMarcaContainingIgnoreCase(String marca);

  List<Producto> findByEstado(EstadoProducto estado);

  List<Producto> findByNombreContainingIgnoreCase(String nombre);

  List<Producto> findByPrecioGreaterThan(BigDecimal precio);

  List<Producto> findByPrecioLessThan(BigDecimal precio);

  List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);

  List<Producto> findByStockLessThan(Integer cantidad);
}
