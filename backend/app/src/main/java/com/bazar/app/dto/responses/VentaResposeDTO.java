package com.bazar.app.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
public class VentaResposeDTO {
  Long codigoVenta;
  LocalDate fecha;
  String estado;
  Long idCliente;
  String nombreCompleto;
  List<DetalleVentaResponseDTO> detalles;
  BigDecimal total;
  LocalDateTime createdAt;
  LocalDateTime updateAt;
}
