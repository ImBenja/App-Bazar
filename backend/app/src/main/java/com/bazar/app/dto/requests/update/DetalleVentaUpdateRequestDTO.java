package com.bazar.app.dto.requests.update;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaUpdateRequestDTO {
  @Min(value = 1, message = "El ID del Producto debe ser un número positivo")
  Long productoId;

  @Min(value = 0, message = "La cantidad del producto no puede ser negativa")
  Integer cantidad;
}
