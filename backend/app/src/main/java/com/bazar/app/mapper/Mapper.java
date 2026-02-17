package com.bazar.app.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bazar.app.dto.requests.create.ClienteCreateRequestDTO;
import com.bazar.app.dto.requests.create.ProductoCreateRequestDTO;
import com.bazar.app.dto.requests.create.VentaCreateRequestDTO;
import com.bazar.app.dto.responses.ClienteResponseDTO;
import com.bazar.app.dto.responses.ProductoResponseDTO;
import com.bazar.app.dto.responses.VentaEstadisticaDTO;
import com.bazar.app.dto.responses.VentaResposeDTO;
import com.bazar.app.dto.responses.EstadisticasDelDiaDTO;
import com.bazar.app.enums.EstadoProducto;
import com.bazar.app.dto.responses.DetalleVentaResponseDTO;
import com.bazar.app.dto.responses.MayorVentaDTO;
import com.bazar.app.error.NotFoundException;
import com.bazar.app.model.Cliente;
import com.bazar.app.model.DetalleVenta;
import com.bazar.app.model.Producto;
import com.bazar.app.model.Venta;
import com.bazar.app.repository.ProductoRespository;

@Component
public class Mapper {

  // MAPEO DE PRODUCTO REQUEST DTO A PRODUCTO
  public static Producto toEntity(ProductoCreateRequestDTO productoRequestDTO) {
    if (productoRequestDTO == null)
      return null;

    return Producto.builder()
        .nombre(productoRequestDTO.getNombre())
        .marca(productoRequestDTO.getMarca())
        .categoria(productoRequestDTO.getCategoria())
        .precio(productoRequestDTO.getPrecio())
        .stock(productoRequestDTO.getStock())
        .build();
  }

  // MAPEO DE PRODUCTO A PRODUCTO RESPONSE DTO

  public static ProductoResponseDTO toResponseDTO(Producto producto) {
    if (producto == null)
      return null;

    return ProductoResponseDTO.builder()
        .codigoProducto(producto.getCodigoProducto())
        .nombre(producto.getNombre())
        .marca(producto.getMarca())
        .categoria(producto.getCategoria())
        .stock(producto.getStock())
        .precio(producto.getPrecio())
        .estado(String.valueOf(producto.getEstado()))
        .createdAt(producto.getCreatedAt())
        .updatedAt(producto.getUpdateAt())
        .build();
  }

  // MAPEO DE CLIENTE REQUEST DTO A CLIENTE

  public static Cliente toEntity(ClienteCreateRequestDTO clienteRequestDTO) {
    if (clienteRequestDTO == null)
      return null;

    return Cliente.builder()
        .nombre(clienteRequestDTO.getNombre())
        .apellido(clienteRequestDTO.getApellido())
        .dni(clienteRequestDTO.getDni())
        .build();
  }

  // MAPEO DE CLIENTE A CLIENTE RESPONSE DTO
  public static ClienteResponseDTO toResponseDTO(Cliente cliente) {
    if (cliente == null)
      return null;

    return ClienteResponseDTO.builder()
        .id(cliente.getId())
        .nombre(cliente.getNombre())
        .apellido(cliente.getApellido())
        .dni(cliente.getDni())
        .estado(String.valueOf(cliente.getEstado()))
        .createdAt(cliente.getCreatedAt())
        .updateAt(cliente.getUpdateAt())
        .build();
  }

  // MAPEO DE VENTA REQUEST DTO A VENTA
  public static Venta toEntity(VentaCreateRequestDTO dto,
      Cliente cliente,
      ProductoRespository productoRepository) {

    Venta venta = Venta.builder()
        .fecha(dto.getFecha())
        .cliente(cliente)
        .build();

    List<DetalleVenta> detalles = dto.getDetalles().stream()
        .map(det -> {

          Producto producto = productoRepository.findById(det.getProductoId())
              .orElseThrow(() -> new NotFoundException("No existe el producto " + det.getProductoId()));

          if (producto.getEstado() == EstadoProducto.INACTIVO || producto.getEstado() == EstadoProducto.DESCONTINUADO
              || producto.getEstado() == EstadoProducto.AGOTADO) {
            throw new IllegalStateException(
                "El producto " + producto.getNombre() + " no está disponible para la venta" + "(estado: "
                    + producto.getEstado() + ")");
          }

          return DetalleVenta.builder()
              .producto(producto)
              .cantidad(det.getCantidad())
              .productoNombre(producto.getNombre())
              .precioUnitario(producto.getPrecio())
              .venta(venta)
              .build();
        })
        .toList();

    venta.setDetalles(detalles);
    return venta;
  }

  // MAPEO DE VENTA A VENTA RESPONSE DTO

  public static VentaResposeDTO toResponseDTO(Venta venta) {
    if (venta == null)
      return null;

    List<DetalleVentaResponseDTO> detalles = venta.getDetalles().stream()
        .map(det -> DetalleVentaResponseDTO.builder()
            .productoId(det.getProducto().getCodigoProducto())
            .productoNombre(det.getProductoNombre())
            .cantidad(det.getCantidad())
            .precioUnitario(det.getPrecioUnitario())
            .subtotal(det.getSubtotal())
            .build())
        .collect(Collectors.toList());

    return VentaResposeDTO.builder()
        .codigoVenta(venta.getCodigoVenta())
        .fecha(venta.getFecha())
        .estado(String.valueOf(venta.getEstado()))
        .idCliente(venta.getCliente().getId())
        .nombreCompleto(venta.getCliente().getNombre() + " " + venta.getCliente().getApellido())
        .detalles(detalles)
        .total(venta.getTotal())
        .createdAt(venta.getCreatedAt())
        .updateAt(venta.getUpdateAt())
        .build();

  }

  public static VentaEstadisticaDTO toResponseEstadisticaDTO(Venta venta) {
    if (venta == null)
      return null;

    List<DetalleVentaResponseDTO> detalles = venta.getDetalles().stream()
        .map(det -> DetalleVentaResponseDTO.builder()
            .productoId(det.getProducto().getCodigoProducto())
            .productoNombre(det.getProducto().getNombre())
            .cantidad(det.getCantidad())
            .precioUnitario(det.getPrecioUnitario())
            .subtotal(det.getSubtotal())
            .build())
        .collect(Collectors.toList());

    return VentaEstadisticaDTO.builder()
        .codigoVenta(venta.getCodigoVenta())
        .idCliente(venta.getCliente().getId())
        .detalles(detalles)
        .total(venta.getTotal())
        .build();

  }

  // MAPEO DE VENTA A MAYOR VENTA
  public static MayorVentaDTO toMayorVentaDTO(Venta venta, Producto producto, DetalleVenta detalleVenta) {
    if (venta == null)
      return null;

    return MayorVentaDTO.builder()
        .codigoVenta(venta.getCodigoVenta())
        .total(venta.getTotal())
        .cantidad(detalleVenta.getCantidad())
        .nombreCliente(venta.getCliente().getNombre())
        .apellidoCliente(venta.getCliente().getApellido())
        .build();
  }

  public static EstadisticasDelDiaDTO toEstadisticasDelDiaDTO(
      List<Venta> ventasValidas,
      List<Venta> pagadas,
      List<Venta> entregadas,
      List<Venta> pendientes,
      List<Venta> canceladas,
      LocalDate fecha) {

    BigDecimal montoTotal = ventasValidas.stream()
        .map(Venta::getTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal montoCancelado = canceladas.stream()
        .map(Venta::getTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal montoPendientes = pendientes.stream()
        .map(Venta::getTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    Set<Long> clientesUnicosSet = new HashSet<>();
    clientesUnicosSet.addAll(ventasValidas.stream().map(v -> v.getCliente().getId()).toList());
    clientesUnicosSet.addAll(pendientes.stream().map(v -> v.getCliente().getId()).toList());
    clientesUnicosSet.addAll(canceladas.stream().map(v -> v.getCliente().getId()).toList());

    BigDecimal ticketPromedio = ventasValidas.isEmpty() ? BigDecimal.ZERO
        : montoTotal.divide(BigDecimal.valueOf(ventasValidas.size()), 2, RoundingMode.HALF_UP);

    EstadisticasDelDiaDTO.EstadisticasDelDiaDTOBuilder builder = EstadisticasDelDiaDTO.builder()
        .fecha(fecha)
        .montoTotalGanado(montoTotal)
        .cantidadVentas(ventasValidas.size())
        .pendientes(pendientes.size())
        .pagadas(pagadas.size())
        .entregadas(entregadas.size())
        .canceladas(canceladas.size())
        .montoCancelado(montoCancelado)
        .montoPendientes(montoPendientes)
        .clientesUnicos(clientesUnicosSet.size())
        .ticketPromedio(ticketPromedio);

    if (!pendientes.isEmpty()) {
      builder.ventasPendientesDetalle(
          pendientes.stream()
              .map(Mapper::toResponseEstadisticaDTO)
              .toList());
    }

    if (!canceladas.isEmpty()) {
      builder.ventasCanceladasDetalle(
          canceladas.stream()
              .map(Mapper::toResponseEstadisticaDTO)
              .toList());
    }

    if (!pagadas.isEmpty()) {
      builder.ventasPagadasDetalle(
          pagadas.stream()
              .map(Mapper::toResponseEstadisticaDTO)
              .toList());
    }

    return builder.build();
  }

}
