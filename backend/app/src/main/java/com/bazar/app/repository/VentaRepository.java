package com.bazar.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazar.app.model.Cliente;
import com.bazar.app.model.Venta;
import java.time.LocalDate;
import com.bazar.app.enums.EstadoVenta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
  List<Venta> findByCliente(Cliente cliente);

  List<Venta> findByFecha(LocalDate fecha);

  List<Venta> findByEstado(EstadoVenta estado);

  List<Venta> findByClienteAndFecha(Cliente cliente, LocalDate fecha);

  List<Venta> findByFechaAndEstadoIn(LocalDate fecha, List<EstadoVenta> estados);

  Venta findTopByOrderByTotalDesc();
}
