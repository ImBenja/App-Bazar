package com.bazar.app.dto.requests.update;

import java.time.LocalDate;
import java.util.List;

import com.bazar.app.dto.requests.create.DetalleVentaCreateRequestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

@Getter
public class VentaUpdateRequestDTO {
  // VALIDAR FECHA QUE SEA ANTERIOR O IGUAL A LA FECHA ACTUAL
  @PastOrPresent(message = "La fecha de la venta no puede ser futura")
  LocalDate fecha;

  @Min(value = 1, message = "El id del cliente debe ser un número positivo")
  Long idCliente;

  List<DetalleVentaCreateRequestDTO> detalles;
}
