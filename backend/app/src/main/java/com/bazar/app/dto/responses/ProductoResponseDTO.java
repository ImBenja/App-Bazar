package com.bazar.app.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class ProductoResponseDTO {
  Long codigoProducto;
  String nombre;
  String marca;
  String categoria;
  BigDecimal precio;
  Integer stock;
  String estado;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}
