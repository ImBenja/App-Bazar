package com.bazar.app.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazar.app.dto.requests.create.ProductoCreateRequestDTO;
import com.bazar.app.dto.requests.update.ProductoUpdateRequestDTO;
import com.bazar.app.dto.responses.ProductoResponseDTO;
import com.bazar.app.enums.EstadoProducto;
import com.bazar.app.service.IProductoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
  @Autowired
  private IProductoService productoService;

  @GetMapping
  public ResponseEntity<List<ProductoResponseDTO>> getProductos() {
    return ResponseEntity.ok(productoService.getProductos());
  }

  @GetMapping("/nombre/{nombre}")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByNombre(@PathVariable String nombre) {
    return ResponseEntity.ok(productoService.getProductosByNombre(nombre));
  }

  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByCategoria(@PathVariable String categoria) {
    return ResponseEntity.ok(productoService.getProductosByCategoria(categoria));
  }

  @GetMapping("/marca/{marca}")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByMarca(@PathVariable String marca) {
    return ResponseEntity.ok(productoService.getProductosByMarca(marca));
  }

  @GetMapping("/estado/{estado}")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByEstado(@PathVariable EstadoProducto estado) {
    return ResponseEntity.ok(productoService.getProductosByEstado(estado));
  }

  @GetMapping("/falta-stock")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosConStockBajo() {
    return ResponseEntity.ok(productoService.getProductosConStockBajo());
  }

  // PRECIOS
  @GetMapping("/precio-menor/{precio}")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByPrecioMenor(@PathVariable BigDecimal precio) {
    return ResponseEntity.ok(productoService.getProductosByPrecioMenorA(precio));
  }

  @GetMapping("/precio-mayor/{precio}")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByPrecioMayor(@PathVariable BigDecimal precio) {
    return ResponseEntity.ok(productoService.getProductosByPrecioMayorA(precio));
  }

  @GetMapping("/precio-rango")
  public ResponseEntity<List<ProductoResponseDTO>> getProductosByRangoDePrecio(
      @RequestParam BigDecimal min, @RequestParam BigDecimal max) {
    return ResponseEntity.ok(productoService.getProductosByRangoDePrecio(min, max));
  }

  @GetMapping("/codigo/{codigo}")
  public ResponseEntity<ProductoResponseDTO> getProductoByCodigo(@PathVariable Long codigo) {
    return ResponseEntity.ok(productoService.getProductoByCodigo(codigo));
  }

  @PostMapping("/crear")
  public ResponseEntity<ProductoResponseDTO> createProducto(
      @Valid @RequestBody ProductoCreateRequestDTO productoRequestDTO) {
    ProductoResponseDTO createdProducto = productoService.createProducto(productoRequestDTO);
    return ResponseEntity.created(URI.create("/productos" + createdProducto.getCodigoProducto())).body(createdProducto);
  }

  @PutMapping("/editar/{codigo}")
  public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long codigo,
      @Valid @RequestBody ProductoCreateRequestDTO productoRequestDTO) {
    return ResponseEntity.ok(productoService.updateProducto(codigo, productoRequestDTO));
  }

  @PatchMapping("/editar/{codigo}")
  public ResponseEntity<ProductoResponseDTO> partialUpdateProducto(@PathVariable Long codigo,
      @RequestBody ProductoUpdateRequestDTO productoUpdateRequestDTO) {
    return ResponseEntity.ok(productoService.patchProducto(codigo, productoUpdateRequestDTO));
  }

  // ESTADOS
  @PatchMapping("/activar/{codigo}")
  public ResponseEntity<ProductoResponseDTO> activarProducto(@PathVariable Long codigo) {
    return ResponseEntity.ok(productoService.activarProducto(codigo));
  }

  @PatchMapping("/inactivar/{codigo}")
  public ResponseEntity<ProductoResponseDTO> inactivarProducto(@PathVariable Long codigo) {
    return ResponseEntity.ok(productoService.inactivarProducto(codigo));
  }

  @PatchMapping("/agotado/{codigo}")
  public ResponseEntity<ProductoResponseDTO> marcarAgotado(@PathVariable Long codigo) {
    return ResponseEntity.ok(productoService.marcarAgotado(codigo));
  }

  @PatchMapping("/descontinuar/{codigo}")
  public ResponseEntity<ProductoResponseDTO> descontinuarProducto(@PathVariable Long codigo) {
    return ResponseEntity.ok(productoService.descontinuarProducto(codigo));
  }

}
