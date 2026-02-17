package com.bazar.app.dto.requests.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class DetalleVentaCreateRequestDTO {
  @NotNull(message = "El ID del producto no puede ser nulo")
  @Min(value = 1, message = "El ID del Producto debe ser un número positivo")
  Long productoId;

  @NotNull(message = "La cantidad del producto no puede ser nula")
  @Min(value = 0, message = "La cantidad del producto no puede ser negativa")
  Integer cantidad;
}
