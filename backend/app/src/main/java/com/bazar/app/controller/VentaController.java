package com.bazar.app.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazar.app.dto.requests.create.VentaCreateRequestDTO;
import com.bazar.app.dto.requests.update.VentaUpdateRequestDTO;
import com.bazar.app.dto.responses.MayorVentaDTO;
import com.bazar.app.dto.responses.VentaResposeDTO;
import com.bazar.app.dto.responses.EstadisticasDelDiaDTO;
import com.bazar.app.enums.EstadoVenta;
import com.bazar.app.service.IVentaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
  @Autowired
  private IVentaService ventaService;

  @GetMapping
  public ResponseEntity<List<VentaResposeDTO>> getVentas() {
    return ResponseEntity.ok(ventaService.getVentas());
  }

  @GetMapping("/cliente/{idCliente}")
  public ResponseEntity<List<VentaResposeDTO>> getVentasByCliente(@PathVariable Long idCliente) {
    return ResponseEntity.ok(ventaService.getVentasByCliente(idCliente));
  }

  @GetMapping("/fecha/{fDate}")
  public ResponseEntity<List<VentaResposeDTO>> getVentasByFecha(@PathVariable LocalDate fDate) {
    return ResponseEntity.ok(ventaService.getVentasByFecha(fDate));
  }

  @GetMapping("/estado/{estado}")
  public ResponseEntity<List<VentaResposeDTO>> getVentasByEstado(@PathVariable EstadoVenta estado) {
    return ResponseEntity.ok(ventaService.getVentasByEstado(estado));
  }

  @GetMapping("/cliente-fecha")
  public ResponseEntity<List<VentaResposeDTO>> getVentasByClienteAndFecha(@RequestParam Long idCliente,
      @RequestParam LocalDate fecha) {
    return ResponseEntity.ok(ventaService.getVentasByClienteAndFecha(idCliente, fecha));
  }

  @GetMapping("/codigo/{codigo}")
  public ResponseEntity<VentaResposeDTO> getVentaByCodigo(@PathVariable Long codigo) {
    return ResponseEntity.ok(ventaService.getVentaByCodigo(codigo));
  }

  @GetMapping("/mayor-venta")
  public ResponseEntity<MayorVentaDTO> getMayorVenta() {
    return ResponseEntity.ok(ventaService.getMayorVenta());
  }

  @GetMapping("/estadistica-dia/{fechaVenta}")
  public ResponseEntity<EstadisticasDelDiaDTO> getVentasDelDia(@PathVariable LocalDate fechaVenta) {
    return ResponseEntity.ok(ventaService.getVentasDelDia(fechaVenta));
  }

  @PostMapping("/crear")
  public ResponseEntity<VentaResposeDTO> createdVenta(@Valid @RequestBody VentaCreateRequestDTO ventaCreateRequestDTO) {
    VentaResposeDTO createdVenta = ventaService.createVenta(ventaCreateRequestDTO);
    return ResponseEntity.created(URI.create("/ventas/" + createdVenta.getCodigoVenta())).body(createdVenta);
  }

  @PutMapping("/editar/{codigo}")
  public ResponseEntity<VentaResposeDTO> updateVenta(@PathVariable Long codigo,
      @Valid @RequestBody VentaCreateRequestDTO ventaCreateRequestDTO) {
    VentaResposeDTO updatedVenta = ventaService.updateVenta(codigo, ventaCreateRequestDTO);
    return ResponseEntity.ok(updatedVenta);
  }

  @PatchMapping("/editar/{codigo}")
  public ResponseEntity<VentaResposeDTO> partialUpdateVenta(@PathVariable Long codigo,
      @RequestBody VentaUpdateRequestDTO ventaUpdateRequestDTO) {
    VentaResposeDTO updatedVenta = ventaService.patchVenta(codigo, ventaUpdateRequestDTO);
    return ResponseEntity.ok(updatedVenta);
  }

  @PatchMapping("/marcar-pagada/{codigo}")
  public ResponseEntity<VentaResposeDTO> marcarPagada(@PathVariable Long codigo) {
    VentaResposeDTO updatedVenta = ventaService.marcarPagada(codigo);
    return ResponseEntity.ok(updatedVenta);
  }

  @PatchMapping("/marcar-entregada/{codigo}")
  public ResponseEntity<VentaResposeDTO> marcarEntregada(@PathVariable Long codigo) {
    VentaResposeDTO updatedVenta = ventaService.marcarEntregada(codigo);
    return ResponseEntity.ok(updatedVenta);
  }

  @PatchMapping("/marcar-cancelada/{codigo}")
  public ResponseEntity<VentaResposeDTO> marcarCancelada(@PathVariable Long codigo) {
    VentaResposeDTO updatedVenta = ventaService.marcarCancelada(codigo);
    return ResponseEntity.ok(updatedVenta);
  }

}
