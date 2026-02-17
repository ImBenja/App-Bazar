package com.bazar.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bazar.app.dto.requests.create.DetalleVentaCreateRequestDTO;
import com.bazar.app.dto.requests.create.VentaCreateRequestDTO;
import com.bazar.app.dto.requests.update.VentaUpdateRequestDTO;
import com.bazar.app.dto.responses.MayorVentaDTO;
import com.bazar.app.dto.responses.VentaResposeDTO;
import com.bazar.app.dto.responses.EstadisticasDelDiaDTO;
import com.bazar.app.enums.EstadoCliente;
import com.bazar.app.enums.EstadoProducto;
import com.bazar.app.enums.EstadoVenta;
import com.bazar.app.error.BusinessException;
import com.bazar.app.error.NotFoundException;
import com.bazar.app.mapper.Mapper;
import com.bazar.app.model.Cliente;
import com.bazar.app.model.DetalleVenta;
import com.bazar.app.model.Producto;
import com.bazar.app.model.Venta;
import com.bazar.app.repository.ClienteRepository;
import com.bazar.app.repository.ProductoRespository;
import com.bazar.app.repository.VentaRepository;

@Service
public class VentaService implements IVentaService {
  @Autowired
  private VentaRepository ventaRepository;

  @Autowired
  private ProductoRespository productoRespository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Override
  public MayorVentaDTO getMayorVenta() {
    Venta venta = ventaRepository.findTopByOrderByTotalDesc();
    if (venta == null) {
      throw new NotFoundException("No se encontró ninguna venta");
    }
    DetalleVenta detalleVenta = venta.getDetalles().get(0);
    Producto producto = detalleVenta.getProducto();
    return Mapper.toMayorVentaDTO(venta, producto, detalleVenta);
  }

  @Override
  public EstadisticasDelDiaDTO getVentasDelDia(LocalDate fecha) {
    List<Venta> ventasDelDia = ventaRepository.findByFecha(fecha);

    if (ventasDelDia.isEmpty()) {
      throw new BusinessException("No se encontraron ventas en la fecha " + fecha);
    }

    List<Venta> pagadas = new ArrayList<>();
    List<Venta> entregadas = new ArrayList<>();
    List<Venta> pendientes = new ArrayList<>();
    List<Venta> canceladas = new ArrayList<>();

    for (Venta venta : ventasDelDia) {
      switch (venta.getEstado()) {
        case PAGADA -> pagadas.add(venta);
        case ENTREGADA -> entregadas.add(venta);
        case PENDIENTE -> pendientes.add(venta);
        case CANCELADA -> canceladas.add(venta);
      }
    }

    List<Venta> ventasValidas = new ArrayList<>();
    ventasValidas.addAll(pagadas);
    ventasValidas.addAll(entregadas);

    return Mapper.toEstadisticasDelDiaDTO(
        ventasValidas,
        pagadas,
        entregadas,
        pendientes,
        canceladas,
        fecha);
  }

  @Override
  public VentaResposeDTO createVenta(VentaCreateRequestDTO ventaCreateRequestDTO) {
    if (ventaCreateRequestDTO == null) {
      throw new IllegalArgumentException("La venta no puedes ser nula");
    }
    Cliente cliente = clienteRepository.findById(ventaCreateRequestDTO.getIdCliente()).orElseThrow(
        () -> new NotFoundException("EL Cliente con ID " + ventaCreateRequestDTO.getIdCliente() + " no existe."));

    if (cliente.getEstado() == EstadoCliente.INACTIVO || cliente.getEstado() == EstadoCliente.BLOQUEADO) {
      throw new BusinessException("El Cliente " + cliente.getNombre() + " " + cliente.getApellido()
          + " no se encuentra disponible para comprar. Estado: " + cliente.getEstado());
    }

    Venta venta = Mapper.toEntity(ventaCreateRequestDTO, cliente, productoRespository);
    if (!venta.validarStock()) {
      throw new BusinessException("Stock insuficiente para uno o más productos");
    }

    venta.getDetalles().forEach(det -> det.setSubtotal(det.calcularSubtotal()));

    venta.setTotal(venta.calcularTotal());

    venta.validarEstadoProductos();

    venta.descontarStock();

    return Mapper.toResponseDTO(ventaRepository.save(venta));
  }

  @Override
  public VentaResposeDTO getVentaByCodigo(Long codigo) {
    Venta venta = ventaRepository.findById(codigo)
        .orElseThrow(() -> new NotFoundException("La venta con Codigo " + codigo + " no fue encontrada"));
    return Mapper.toResponseDTO(venta);
  }

  @Override
  public List<VentaResposeDTO> getVentas() {
    if (ventaRepository.findAll().isEmpty()) {
      throw new NotFoundException("No se encontraron ventas");
    }
    return ventaRepository.findAll().stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public List<VentaResposeDTO> getVentasByCliente(Long idCliente) {
    if (idCliente == null) {
      throw new IllegalArgumentException("El ID del cliente no puede ser nulo");
    }
    Cliente cliente = clienteRepository.findById(idCliente)
        .orElseThrow(() -> new NotFoundException("El Cliente con ID " + idCliente + " no fue encontrado."));
    if (ventaRepository.findByCliente(cliente).isEmpty()) {
      throw new NotFoundException("No se encontraron ventas para el Cliente con ID " + idCliente);
    }
    return ventaRepository.findByCliente(cliente).stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public List<VentaResposeDTO> getVentasByClienteAndFecha(Long idCliente, LocalDate fDate) {
    if (idCliente == null) {
      throw new IllegalArgumentException("El ID del cliente no puede ser nulo");
    }
    if (fDate == null) {
      throw new IllegalArgumentException("La fecha no puede ser nula");
    }
    Cliente cliente = clienteRepository.findById(idCliente)
        .orElseThrow(() -> new NotFoundException("El Cliente con ID " + idCliente + " no fue encontrado."));
    if (ventaRepository.findByClienteAndFecha(cliente, fDate).isEmpty()) {
      throw new NotFoundException(
          "No se encontraron ventas para el Cliente con ID " + idCliente + " en la fecha " + fDate);
    }
    return ventaRepository.findByClienteAndFecha(cliente, fDate).stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public List<VentaResposeDTO> getVentasByEstado(EstadoVenta estado) {
    if (estado == null) {
      throw new IllegalArgumentException("El estado de la venta no puede ser nulo");
    }
    if (ventaRepository.findByEstado(estado).isEmpty()) {
      throw new NotFoundException("No se encontraron ventas con el estado " + estado);
    }
    return ventaRepository.findByEstado(estado).stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public List<VentaResposeDTO> getVentasByFecha(LocalDate fDate) {
    if (fDate == null) {
      throw new IllegalArgumentException("La fecha no puede ser nula");
    }
    if (ventaRepository.findByFecha(fDate).isEmpty()) {
      throw new NotFoundException("No se encontraron ventas para la fecha " + fDate);
    }
    return ventaRepository.findByFecha(fDate).stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public VentaResposeDTO marcarCancelada(Long codigo) {
    Venta venta = ventaRepository.findById(codigo)
        .orElseThrow(() -> new NotFoundException("La venta con Codigo " + codigo + " no fue encontrada"));
    if (venta.getEstado() == EstadoVenta.ENTREGADA) {
      throw new BusinessException(
          "La Venta con Codigo " + codigo + " ya se encuentra ENTREGADA por lo que no se puede CANCELAR");
    }
    if (venta.getEstado() == EstadoVenta.CANCELADA) {
      throw new BusinessException("La Venta con Codigo " + codigo + " ya se encuentra CANCELADA");
    }
    venta.cambiarEstado(EstadoVenta.CANCELADA);
    venta.restaurarStock();
    return Mapper.toResponseDTO(ventaRepository.save(venta));
  }

  @Override
  public VentaResposeDTO marcarEntregada(Long codigo) {
    Venta venta = ventaRepository.findById(codigo)
        .orElseThrow(() -> new NotFoundException("La venta con Codigo " + codigo + " no fue encontrada"));
    if (venta.getEstado() == EstadoVenta.CANCELADA) {
      throw new BusinessException(
          "La Venta con Codigo " + codigo + " se encuentra CANCELADA por lo que no se puede ENTREGAR");
    }
    if (venta.getEstado() == EstadoVenta.ENTREGADA) {
      throw new BusinessException("La Venta con Codigo " + codigo + " ya se encuentra ENTREGADA");
    }
    if (venta.getEstado() == EstadoVenta.PENDIENTE) {
      throw new BusinessException(
          "La Venta con Codigo " + codigo + " se encuentra en estado PENDIENTE por lo que no se puede ENTREGAR");
    }
    venta.cambiarEstado(EstadoVenta.ENTREGADA);
    return Mapper.toResponseDTO(ventaRepository.save(venta));
  }

  @Override
  public VentaResposeDTO marcarPagada(Long codigo) {
    Venta venta = ventaRepository.findById(codigo)
        .orElseThrow(() -> new NotFoundException("La venta con Codigo " + codigo + " no fue encontrada"));
    if (venta.getEstado() == EstadoVenta.CANCELADA) {
      throw new BusinessException(
          "La Venta con Codigo " + codigo + " se encuentra CANCELADA por lo que no se puede PAGAR");
    }
    if (venta.getEstado() == EstadoVenta.PAGADA) {
      throw new BusinessException("La Venta con Codigo " + codigo + " ya se encuentra PAGADA");
    }
    if (venta.getEstado() == EstadoVenta.ENTREGADA) {
      throw new BusinessException(
          "La Venta con Codigo " + codigo + " se encuentra ENTREGADA por lo que no se puede PAGAR");
    }
    venta.cambiarEstado(EstadoVenta.PAGADA);
    return Mapper.toResponseDTO(ventaRepository.save(venta));
  }

  @Override
  public VentaResposeDTO updateVenta(Long codigo, VentaCreateRequestDTO ventaCreateRequestDTO) {
    Venta venta = ventaRepository.findById(codigo)
        .orElseThrow(() -> new NotFoundException("La venta con Codigo " + codigo + " no fue encontrada"));

    validarVentaEditable(venta);

    boolean mismaFecha = ventaCreateRequestDTO.getFecha() != null
        && ventaCreateRequestDTO.getFecha().equals(venta.getFecha());
    boolean mismoCliente = ventaCreateRequestDTO.getIdCliente() != null
        && ventaCreateRequestDTO.getIdCliente().equals(venta.getCliente().getId());
    boolean mismosDetalles = ventaCreateRequestDTO.getDetalles() != null
        && ventaCreateRequestDTO.getDetalles().isEmpty() && venta.getDetalles().isEmpty();

    if (mismaFecha && mismoCliente && mismosDetalles) {
      throw new BusinessException("No se enviaron cambios para actualizar");
    }

    if (ventaCreateRequestDTO.getDetalles() != null) {
      agregarDetalles(venta, ventaCreateRequestDTO.getDetalles());
      venta.setTotal(venta.calcularTotal());
      venta.validarEstadoProductos();
    }

    venta.setFecha(ventaCreateRequestDTO.getFecha());

    if (ventaCreateRequestDTO.getIdCliente() != null) {
      Cliente cliente = clienteRepository.findById(ventaCreateRequestDTO.getIdCliente())
          .orElseThrow(() -> new NotFoundException(
              "El Cliente con ID " + ventaCreateRequestDTO.getIdCliente() + " no fue encontrado."));
      if (cliente.getEstado() == EstadoCliente.INACTIVO || cliente.getEstado() == EstadoCliente.BLOQUEADO) {
        throw new BusinessException("El Cliente " + cliente.getNombre() + " " + cliente.getApellido()
            + " no se encuentra disponible para comprar. Estado: " + cliente.getEstado());
      }
      venta.setCliente(cliente);
    }

    return Mapper.toResponseDTO(ventaRepository.save(venta));
  }

  @Override
  public VentaResposeDTO patchVenta(Long codigo, VentaUpdateRequestDTO ventaUpdateRequestDTO) {
    Venta venta = ventaRepository.findById(codigo)
        .orElseThrow(() -> new NotFoundException("La venta con Codigo " + codigo + " no fue encontrada"));

    validarVentaEditable(venta);

    boolean hayCambiosEnDetalles = ventaUpdateRequestDTO.getDetalles() != null
        && !ventaUpdateRequestDTO.getDetalles().isEmpty();

    if (ventaUpdateRequestDTO.getFecha() == null
        && ventaUpdateRequestDTO.getIdCliente() == null
        && !hayCambiosEnDetalles) {
      throw new BusinessException("No se enviaron campos para actualizar");
    }

    if (hayCambiosEnDetalles) {
      agregarDetalles(venta, ventaUpdateRequestDTO.getDetalles());
      venta.setTotal(venta.calcularTotal());
      venta.validarEstadoProductos();
    }

    if (ventaUpdateRequestDTO.getFecha() != null) {
      venta.setFecha(ventaUpdateRequestDTO.getFecha());
    }

    if (ventaUpdateRequestDTO.getIdCliente() != null) {
      Cliente cliente = clienteRepository.findById(ventaUpdateRequestDTO.getIdCliente())
          .orElseThrow(() -> new NotFoundException(
              "El Cliente con ID " + ventaUpdateRequestDTO.getIdCliente() + " no fue encontrado."));
      if (cliente.getEstado() == EstadoCliente.INACTIVO || cliente.getEstado() == EstadoCliente.BLOQUEADO) {
        throw new BusinessException("El Cliente " + cliente.getNombre() + " " + cliente.getApellido()
            + " no se encuentra disponible para comprar. Estado: " + cliente.getEstado());
      }
      venta.setCliente(cliente);
    }

    return Mapper.toResponseDTO(ventaRepository.save(venta));
  }

  // ========================
  // MÉTODOS PRIVADOS
  // ========================

  private void validarVentaEditable(Venta venta) {
    if (venta.getEstado() == EstadoVenta.CANCELADA
        || venta.getEstado() == EstadoVenta.ENTREGADA
        || venta.getEstado() == EstadoVenta.PAGADA) {
      throw new BusinessException(
          "La Venta con Codigo " + venta.getCodigoVenta() +
              " se encuentra en estado " + venta.getEstado() +
              " y no se puede editar");
    }
  }

  private void agregarDetalles(Venta venta, List<DetalleVentaCreateRequestDTO> detalles) {

    for (DetalleVentaCreateRequestDTO det : detalles) {

      if (det.getProductoId() == null) {
        throw new IllegalArgumentException("El ID del producto no puede ser nulo");
      }

      if (det.getCantidad() == null || det.getCantidad() <= 0) {
        throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
      }

      Producto producto = productoRespository.findById(det.getProductoId())
          .orElseThrow(() -> new NotFoundException("No existe el producto " + det.getProductoId()));

      if (producto.getEstado() == EstadoProducto.INACTIVO
          || producto.getEstado() == EstadoProducto.DESCONTINUADO) {
        throw new BusinessException(
            "El producto " + producto.getNombre() +
                " no está disponible para la venta (estado: " + producto.getEstado() + ")");
      }

      if (producto.getStock() < det.getCantidad()) {
        throw new BusinessException("Stock insuficiente para el producto " + producto.getNombre());
      }

      DetalleVenta existente = venta.getDetalles().stream()
          .filter(d -> d.getProducto().getCodigoProducto().equals(producto.getCodigoProducto()))
          .findFirst()
          .orElse(null);

      if (existente != null) {
        existente.setCantidad(existente.getCantidad() + det.getCantidad());
        existente.setSubtotal(existente.calcularSubtotal());
      } else {
        DetalleVenta nuevo = DetalleVenta.builder()
            .venta(venta)
            .producto(producto)
            .productoNombre(producto.getNombre())
            .cantidad(det.getCantidad())
            .precioUnitario(producto.getPrecio())
            .subtotal(producto.getPrecio().multiply(BigDecimal.valueOf(det.getCantidad())))
            .build();

        venta.getDetalles().add(nuevo);
      }
      producto.setStock(producto.getStock() - det.getCantidad());
    }
  }

}
