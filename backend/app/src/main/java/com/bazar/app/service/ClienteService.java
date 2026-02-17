package com.bazar.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bazar.app.dto.requests.create.ClienteCreateRequestDTO;
import com.bazar.app.dto.requests.update.ClienteUpdateRequestDTO;
import com.bazar.app.dto.responses.ClienteResponseDTO;
import com.bazar.app.enums.EstadoCliente;
import com.bazar.app.error.BusinessException;
import com.bazar.app.error.NotFoundException;
import com.bazar.app.mapper.Mapper;
import com.bazar.app.model.Cliente;
import com.bazar.app.repository.ClienteRepository;

@Service
public class ClienteService implements IClienteService {
  @Autowired
  private ClienteRepository clienteRepository;

  @Override
  public ClienteResponseDTO activarCliente(Long id) {
    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("El cliente con ID " + id + " no existe."));

    if (cliente.getEstado() == EstadoCliente.ACTIVO) {
      throw new BusinessException(
          "El Cliente con ID " + id + " ya se encuentra ACTIVO");
    }
    if (cliente.getEstado() == EstadoCliente.BLOQUEADO) {
      throw new BusinessException(
          "El Cliente con ID " + id + " se encuntra BLOQUEADO por lo que no se puede ACTIVAR");
    }
    cliente.activar();
    return Mapper.toResponseDTO(clienteRepository.save(cliente));
  }

  @Override
  public ClienteResponseDTO bloquearCliente(Long id) {
    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("El cliente con ID " + id + " no existe."));

    if (cliente.getEstado() == EstadoCliente.BLOQUEADO) {
      throw new BusinessException(
          "El Cliente con ID " + id + " ya se encuentra BLOQUEADO");
    }
    cliente.bloquear();
    return Mapper.toResponseDTO(clienteRepository.save(cliente));
  }

  @Override
  public ClienteResponseDTO createCliente(ClienteCreateRequestDTO clienteRequestDTO) {
    if (clienteRequestDTO == null) {
      throw new BusinessException("El cliente no puede ser nulo");
    }

    Cliente cliente = Mapper.toEntity(clienteRequestDTO);
    if (clienteRepository.existsByDni(cliente.getDni())) {
      throw new BusinessException("El cliente con DNI " + cliente.getDni() + " ya existe.");
    }
    return Mapper.toResponseDTO(clienteRepository.save(cliente));
  }

  @Override
  public ClienteResponseDTO getClienteById(Long id) {
    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("El cliente con ID " + id + " no existe."));
    return Mapper.toResponseDTO(cliente);
  }

  @Override
  public List<ClienteResponseDTO> getClientes() {
    if (clienteRepository.findAll().isEmpty()) {
      throw new NotFoundException("No se encontraron clientes");
    }
    return clienteRepository.findAll().stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public List<ClienteResponseDTO> getClientesByEstado(EstadoCliente estado) {
    if (estado == null) {
      throw new BusinessException("El estado del cliente no puede ser nulo");
    }
    List<ClienteResponseDTO> cliEst = clienteRepository.findByEstado(estado).stream()
        .map(Mapper::toResponseDTO).toList();
    if (cliEst.isEmpty()) {
      throw new NotFoundException("No se encontraron clientes con el estado " + estado);
    }
    return cliEst;
  }

  @Override
  public ClienteResponseDTO inactivarCliente(Long id) {
    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("El cliente con ID " + id + " no existe."));

    if (cliente.getEstado() == EstadoCliente.INACTIVO) {
      throw new BusinessException(
          "El Cliente con ID " + id + " ya se encuentra INACTIVO");
    }
    if (cliente.getEstado() == EstadoCliente.BLOQUEADO) {
      throw new BusinessException(
          "El Cliente con ID " + id + " se encuntra BLOQUEADO por lo que no se puede INACTIVAR");
    }
    cliente.inactivar();
    return Mapper.toResponseDTO(clienteRepository.save(cliente));
  }

  @Override
  public ClienteResponseDTO updateCliente(Long id, ClienteCreateRequestDTO dto) {

    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("El cliente con ID " + id + " no existe."));

    if (cliente.getEstado() == EstadoCliente.BLOQUEADO) {
      throw new BusinessException("El cliente con ID " + id + " está BLOQUEADO y no se puede editar");
    }

    boolean mismoNombre = dto.getNombre().equalsIgnoreCase(cliente.getNombre());
    boolean mismoApellido = dto.getApellido().equalsIgnoreCase(cliente.getApellido());
    boolean mismoDni = dto.getDni().equalsIgnoreCase(cliente.getDni());

    if (mismoNombre && mismoApellido && mismoDni) {
      throw new BusinessException("No hay cambios para actualizar en el cliente con ID " + id);
    }

    cliente.setNombre(dto.getNombre());
    cliente.setApellido(dto.getApellido());
    cliente.setDni(dto.getDni());

    return Mapper.toResponseDTO(clienteRepository.save(cliente));
  }

  @Override
  public ClienteResponseDTO patchCliente(Long id, ClienteUpdateRequestDTO dto) {

    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("El cliente con ID " + id + " no existe."));

    if (cliente.getEstado() == EstadoCliente.BLOQUEADO) {
      throw new BusinessException("El cliente con ID " + id + " está BLOQUEADO y no se puede editar");
    }

    if (dto.getNombre() == null && dto.getApellido() == null && dto.getDni() == null) {
      throw new BusinessException("No se enviaron campos para actualizar");
    }

    if (dto.getNombre() != null)
      cliente.setNombre(dto.getNombre());
    if (dto.getApellido() != null)
      cliente.setApellido(dto.getApellido());
    if (dto.getDni() != null)
      cliente.setDni(dto.getDni());

    return Mapper.toResponseDTO(clienteRepository.save(cliente));
  }

}
