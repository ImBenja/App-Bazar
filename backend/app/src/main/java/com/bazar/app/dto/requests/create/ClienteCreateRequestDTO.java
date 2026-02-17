package com.bazar.app.dto.requests.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class ClienteCreateRequestDTO {
  @NotBlank(message = "El nombre del cliente no puede estar vacio")
  @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
  String nombre;

  @NotBlank(message = "El apellido del cliente no puede estar vacio")
  @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
  String apellido;

  @NotBlank(message = "El dni del cliente no puede estar vacio")
  @Pattern(regexp = "^[0-9]{1,2}[\\.\\-]?[0-9]{3}[\\.\\-]?[0-9]{3}$", message = "Formato de DNI invalido")
  String dni;
}
