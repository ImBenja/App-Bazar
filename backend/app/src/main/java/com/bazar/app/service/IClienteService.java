package com.bazar.app.service;

import java.util.List;

import com.bazar.app.dto.requests.create.ClienteCreateRequestDTO;
import com.bazar.app.dto.requests.update.ClienteUpdateRequestDTO;
import com.bazar.app.dto.responses.ClienteResponseDTO;
import com.bazar.app.enums.EstadoCliente;

public interface IClienteService {
  List<ClienteResponseDTO> getClientes();

  List<ClienteResponseDTO> getClientesByEstado(EstadoCliente estado);

  ClienteResponseDTO getClienteById(Long id);

  ClienteResponseDTO createCliente(ClienteCreateRequestDTO clienteRequestDTO);

  ClienteResponseDTO updateCliente(Long id, ClienteCreateRequestDTO clienteRequestDTO);

  ClienteResponseDTO patchCliente(Long id, ClienteUpdateRequestDTO clienteUpdateRequestDTO);

  ClienteResponseDTO activarCliente(Long id);

  ClienteResponseDTO inactivarCliente(Long id);

  ClienteResponseDTO bloquearCliente(Long id);

}
