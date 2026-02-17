package com.bazar.app.dto.responses;

import java.math.BigDecimal;

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
public class DetalleVentaResponseDTO {
  Long productoId;
  String productoNombre;
  Integer cantidad;
  BigDecimal precioUnitario;
  BigDecimal subtotal;
}
