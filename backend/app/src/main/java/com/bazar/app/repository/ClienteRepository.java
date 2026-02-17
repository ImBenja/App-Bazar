package com.bazar.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazar.app.model.Cliente;
import com.bazar.app.enums.EstadoCliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  List<Cliente> findByEstado(EstadoCliente estado);

  boolean existsByDni(String dni);
}
