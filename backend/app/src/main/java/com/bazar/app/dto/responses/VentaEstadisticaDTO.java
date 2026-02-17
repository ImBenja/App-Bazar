package com.bazar.app.dto.responses;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaEstadisticaDTO {
  Long codigoVenta;
  Long idCliente;
  List<DetalleVentaResponseDTO> detalles;
  BigDecimal total;
}
