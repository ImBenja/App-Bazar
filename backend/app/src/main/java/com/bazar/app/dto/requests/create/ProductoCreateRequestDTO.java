package com.bazar.app.dto.requests.create;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ProductoCreateRequestDTO {
  // VALIDACIONES
  @NotBlank(message = "El nombre del producto no puede estar vacío")
  @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres")
  String nombre;

  @NotBlank(message = "La marca del producto no puede estar vacía")
  @Size(min = 2, max = 50, message = "La marca del producto debe tener entre 2 y 50 caracteres")
  String marca;

  @NotBlank(message = "La categoria del producto no puede estar vacía")
  @Size(min = 2, max = 50, message = "La categoria del producto debe tener entre 2 y 50 caracteres")
  String categoria;

  @NotNull(message = "El precio del producto no puede ser nulo")
  @DecimalMin(value = "0.01", inclusive = true, message = "El precio del producto debe ser mayor a 0")
  @Digits(integer = 10, fraction = 2, message = "El precio del producto debe tener hasta 10 dígitos enteros y 2 decimales")
  BigDecimal precio;

  @NotNull(message = "El stock del producto no puede ser nulo")
  @Min(value = 0, message = "El stock del producto no puede ser negativo")
  Integer stock;
}
