package com.bazar.app.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class EstadisticasDelDiaDTO {
  private LocalDate fecha;
  private BigDecimal montoTotalGanado;
  private Integer cantidadVentas; // Ventas exitosas (PAGADAS + ENTREGADAS)

  // Desglose por estado
  private Integer pendientes;
  private Integer pagadas;
  private Integer entregadas;
  private Integer canceladas;

  // Montos por estado
  private BigDecimal montoPendientes;
  private BigDecimal montoCancelado;

  // Detalles (opcional - peuden ponerse solo si se necesitan)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<VentaEstadisticaDTO> ventasPendientesDetalle;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<VentaEstadisticaDTO> ventasPagadasDetalle;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<VentaEstadisticaDTO> ventasCanceladasDetalle;

  // Métricas adicionales
  private Integer clientesUnicos;
  private BigDecimal ticketPromedio;
}
