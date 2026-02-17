package com.bazar.app.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazar.app.dto.requests.create.ClienteCreateRequestDTO;
import com.bazar.app.dto.requests.update.ClienteUpdateRequestDTO;
import com.bazar.app.dto.responses.ClienteResponseDTO;
import com.bazar.app.enums.EstadoCliente;
import com.bazar.app.service.IClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
  @Autowired
  private IClienteService clienteService;

  @GetMapping
  public ResponseEntity<List<ClienteResponseDTO>> getClientes() {
    return ResponseEntity.ok(clienteService.getClientes());
  }

  @GetMapping("/estado/{estadoCliente}")
  public ResponseEntity<List<ClienteResponseDTO>> getClientesByEstado(@PathVariable EstadoCliente estadoCliente) {
    return ResponseEntity.ok(clienteService.getClientesByEstado(estadoCliente));
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
    return ResponseEntity.ok(clienteService.getClienteById(id));
  }

  @PostMapping("/crear")
  public ResponseEntity<ClienteResponseDTO> createdCliente(
      @Valid @RequestBody ClienteCreateRequestDTO clienteRequestDTO) {
    ClienteResponseDTO clienteCreado = clienteService.createCliente(clienteRequestDTO);

    return ResponseEntity.created(URI.create("/clientes" + clienteCreado.getId())).body(clienteCreado);
  }

  @PutMapping("/editar/{id}")
  public ResponseEntity<ClienteResponseDTO> updateCliente(@PathVariable Long id,
      @Valid @RequestBody ClienteCreateRequestDTO clienteRequestDTO) {
    return ResponseEntity.ok(clienteService.updateCliente(id, clienteRequestDTO));
  }

  @PatchMapping("/editar/{id}")
  public ClienteResponseDTO updateParcial(
      @PathVariable Long id,
      @Valid @RequestBody ClienteUpdateRequestDTO dto) {
    return clienteService.patchCliente(id, dto);
  }

  @PatchMapping("/activar/{id}")
  public ResponseEntity<ClienteResponseDTO> activarCliente(@PathVariable Long id) {
    return ResponseEntity.ok(clienteService.activarCliente(id));
  }

  @PatchMapping("/inactivar/{id}")
  public ResponseEntity<ClienteResponseDTO> inactivarCliente(@PathVariable Long id) {
    return ResponseEntity.ok(clienteService.inactivarCliente(id));
  }

  @PatchMapping("/bloquear/{id}")
  public ResponseEntity<ClienteResponseDTO> bloquearCliente(@PathVariable Long id) {
    return ResponseEntity.ok(clienteService.bloquearCliente(id));
  }

}
