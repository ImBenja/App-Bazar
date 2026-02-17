package com.bazar.app.dto.requests.update;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoUpdateRequestDTO {
  @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres")
  String nombre;

  @Size(min = 2, max = 50, message = "La marca del producto debe tener entre 2 y 50 caracteres")
  String marca;

  @Size(min = 2, max = 50, message = "La categoria del producto debe tener entre 2 y 50 caracteres")
  String categoria;

  @DecimalMin(value = "0.01", inclusive = true, message = "El precio del producto debe ser mayor a 0")
  @Digits(integer = 10, fraction = 2, message = "El precio del producto debe tener hasta 10 dígitos enteros y 2 decimales")
  BigDecimal precio;

  @Min(value = 0, message = "El stock del producto no puede ser negativo")
  Integer stock;
}
