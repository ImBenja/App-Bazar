package com.bazar.app.dto.requests.create;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
public class VentaCreateRequestDTO {
  @NotNull(message = "La fecha de la venta no puede ser nula")
  @PastOrPresent(message = "La fecha de la venta no puede ser futura")
  LocalDate fecha;

  @NotNull(message = "El id del cliente no puede ser nulo")
  @Min(value = 1, message = "El id del cliente debe ser un número positivo")
  Long idCliente;

  @NotNull(message = "La lista de detalles de venta no puede ser nula")
  List<DetalleVentaCreateRequestDTO> detalles;
}
