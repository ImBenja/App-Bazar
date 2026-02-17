package com.bazar.app.dto.requests.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUpdateRequestDTO {
  @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
  String nombre;

  @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
  String apellido;

  @Pattern(regexp = "^[0-9]{1,2}[\\.\\-]?[0-9]{3}[\\.\\-]?[0-9]{3}$", message = "Formato de DNI invalido")
  String dni;
}
