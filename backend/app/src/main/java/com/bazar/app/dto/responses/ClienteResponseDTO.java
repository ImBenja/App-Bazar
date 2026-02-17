package com.bazar.app.dto.responses;

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
public class ClienteResponseDTO {
  Long id;
  String nombre;
  String apellido;
  String dni;
  String estado;
  LocalDateTime createdAt;
  LocalDateTime updateAt;
}
