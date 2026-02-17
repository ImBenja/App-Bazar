package com.bazar.app.service;

import java.time.LocalDate;
import java.util.List;

import com.bazar.app.dto.requests.create.VentaCreateRequestDTO;
import com.bazar.app.dto.requests.update.VentaUpdateRequestDTO;
import com.bazar.app.dto.responses.MayorVentaDTO;
import com.bazar.app.dto.responses.VentaResposeDTO;
import com.bazar.app.dto.responses.EstadisticasDelDiaDTO;
import com.bazar.app.enums.EstadoVenta;

public interface IVentaService {
  List<VentaResposeDTO> getVentas();

  List<VentaResposeDTO> getVentasByCliente(Long idCliente);

  List<VentaResposeDTO> getVentasByFecha(LocalDate fDate);

  List<VentaResposeDTO> getVentasByEstado(EstadoVenta estado);

  List<VentaResposeDTO> getVentasByClienteAndFecha(Long idCliente, LocalDate fDate);

  VentaResposeDTO getVentaByCodigo(Long codigo);

  VentaResposeDTO createVenta(VentaCreateRequestDTO ventaCreateRequestDTO);

  VentaResposeDTO updateVenta(Long codigo, VentaCreateRequestDTO ventaCreateRequestDTO);

  VentaResposeDTO patchVenta(Long codigo, VentaUpdateRequestDTO ventaUpdateRequestDTO);

  VentaResposeDTO marcarPagada(Long codigo);

  VentaResposeDTO marcarEntregada(Long codigo);

  VentaResposeDTO marcarCancelada(Long codigo);

  MayorVentaDTO getMayorVenta();

  EstadisticasDelDiaDTO getVentasDelDia(LocalDate fecha);
}
