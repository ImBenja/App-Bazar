package com.bazar.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bazar.app.dto.requests.create.ProductoCreateRequestDTO;
import com.bazar.app.dto.requests.update.ProductoUpdateRequestDTO;
import com.bazar.app.dto.responses.ProductoResponseDTO;
import com.bazar.app.enums.EstadoProducto;
import com.bazar.app.error.BusinessException;
import com.bazar.app.error.NotFoundException;
import com.bazar.app.mapper.Mapper;
import com.bazar.app.model.Producto;
import com.bazar.app.repository.ProductoRespository;

@Service
public class ProductoService implements IProductoService {
  @Autowired
  private ProductoRespository productoRespository;

  @Override
  public List<ProductoResponseDTO> getProductosConStockBajo() {
    List<Producto> productos = productoRespository.findByStockLessThan(5);
    if (productos.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con stock menor a 5");
    }
    return productos.stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public ProductoResponseDTO activarProducto(Long codigoProducto) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));

    if (producto.getEstado() == EstadoProducto.DESCONTINUADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuntra DISCONTINUADO por lo que no se puede ACTIVAR");
    }
    if (producto.getEstado() == EstadoProducto.ACTIVO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " ya se encuentra ACTIVO");
    }
    if (producto.getEstado() == EstadoProducto.AGOTADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuentra AGOTADO por lo que no se puede ACTIVAR");
    }
    producto.activar();
    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

  @Override
  public ProductoResponseDTO createProducto(ProductoCreateRequestDTO productoRequestDTO) {
    if (productoRequestDTO == null) {
      throw new IllegalArgumentException("El producto no puede ser nulo");
    }
    Producto producto = Mapper.toEntity(productoRequestDTO);
    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

  @Override
  public ProductoResponseDTO descontinuarProducto(Long codigoProducto) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));
    producto.descontinuar();
    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

  @Override
  public ProductoResponseDTO getProductoByCodigo(Long codigoProducto) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));
    return Mapper.toResponseDTO(producto);
  }

  @Override
  public List<ProductoResponseDTO> getProductos() {
    if (productoRespository.findAll().isEmpty()) {
      throw new NotFoundException("No se encontraron productos");
    }
    return productoRespository.findAll().stream().map(Mapper::toResponseDTO).toList();
  }

  @Override
  public List<ProductoResponseDTO> getProductosByCategoria(String categoria) {
    if (categoria == null || categoria.isBlank()) {
      throw new IllegalArgumentException("La categoria del producto no puede ser nula o estar vacia");
    }
    List<ProductoResponseDTO> producCat = productoRespository.findByCategoriaContainingIgnoreCase(categoria).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producCat.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con la categoria " + categoria);
    }
    return producCat;
  }

  @Override
  public List<ProductoResponseDTO> getProductosByEstado(EstadoProducto estado) {
    if (estado == null) {
      throw new IllegalArgumentException("El estado del producto no puede ser nulo");
    }
    List<ProductoResponseDTO> producEst = productoRespository.findByEstado(estado).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producEst.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con el estado " + estado);
    }
    return producEst;
  }

  @Override
  public List<ProductoResponseDTO> getProductosByMarca(String marca) {
    if (marca == null || marca.isBlank()) {
      throw new IllegalArgumentException("La marca del producto no puede ser nula o estar vacia");
    }
    List<ProductoResponseDTO> producMar = productoRespository.findByMarcaContainingIgnoreCase(marca).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producMar.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con la marca " + marca);
    }
    return producMar;
  }

  @Override
  public List<ProductoResponseDTO> getProductosByNombre(String nombre) {
    if (nombre == null || nombre.isBlank()) {
      throw new IllegalArgumentException("El nombre del producto no puede ser nulo o estar vacio");
    }
    List<ProductoResponseDTO> producNom = productoRespository.findByNombreContainingIgnoreCase(nombre).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producNom.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con el nombre " + nombre);
    }
    return producNom;
  }

  @Override
  public List<ProductoResponseDTO> getProductosByPrecioMayorA(BigDecimal precio) {
    if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("El precio del producto no puede ser nulo o negativo");
    }
    List<ProductoResponseDTO> producPre = productoRespository.findByPrecioGreaterThan(precio).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producPre.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con el precio mayor a " + precio);
    }
    return producPre;
  }

  @Override
  public List<ProductoResponseDTO> getProductosByPrecioMenorA(BigDecimal precio) {
    if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("El precio del producto no puede ser nulo o negativo");
    }
    List<ProductoResponseDTO> producPre = productoRespository.findByPrecioLessThan(precio).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producPre.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con el precio menor a " + precio);
    }
    return producPre;
  }

  @Override
  public List<ProductoResponseDTO> getProductosByRangoDePrecio(BigDecimal min, BigDecimal max) {
    if (min == null || max == null || min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0
        || min.compareTo(max) > 0) {
      throw new IllegalArgumentException(
          "El rango de precio del producto no puede ser nulo, negativo o el valor minimo no puede ser mayor al maximo");
    }
    List<ProductoResponseDTO> producPre = productoRespository.findByPrecioBetween(min, max).stream()
        .map(Mapper::toResponseDTO).toList();
    if (producPre.isEmpty()) {
      throw new NotFoundException("No se encontraron productos con el precio entre " + min + " y " + max);
    }
    return producPre;
  }

  @Override
  public ProductoResponseDTO inactivarProducto(Long codigoProducto) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));

    if (producto.getEstado() == EstadoProducto.DESCONTINUADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuntra DISCONTINUADO por lo que no se puede INACTIVAR");
    }
    if (producto.getEstado() == EstadoProducto.INACTIVO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " ya se encuentra INACTIVO");
    }
    if (producto.getEstado() == EstadoProducto.AGOTADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuentra AGOTADO por lo que no se puede INACTIVAR");
    }
    producto.inactivar();
    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

  @Override
  public ProductoResponseDTO marcarAgotado(Long codigoProducto) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));

    if (producto.getEstado() == EstadoProducto.DESCONTINUADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto
              + " se encuntra DISCONTINUADO por lo que no se puede marcar AGOTADO");
    }
    if (producto.getEstado() == EstadoProducto.INACTIVO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuntra INACTIVO por lo que no se puede marcar AGOTADO");
    }
    if (producto.getEstado() == EstadoProducto.AGOTADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " ya se encuentra AGOTADO");
    }
    producto.marcarAgotado();
    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

  @Override
  public ProductoResponseDTO updateProducto(Long codigoProducto, ProductoCreateRequestDTO productoRequestDTO) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));

    if (producto.getEstado() == EstadoProducto.DESCONTINUADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuentra DESCONTINUADO por lo que no se puede editar");
    }

    boolean mismoNombre = productoRequestDTO.getNombre().equalsIgnoreCase(producto.getNombre());
    boolean mismaCategoria = productoRequestDTO.getCategoria().equalsIgnoreCase(producto.getCategoria());
    boolean mismaMarca = productoRequestDTO.getMarca().equalsIgnoreCase(producto.getMarca());
    boolean mismoPrecio = productoRequestDTO.getPrecio().compareTo(producto.getPrecio()) == 0;
    boolean mismoStock = productoRequestDTO.getStock() == producto.getStock();

    if (mismoNombre && mismaCategoria && mismaMarca && mismoPrecio && mismoStock) {
      throw new BusinessException("No hay cambios para actualizar en el Producto con Codigo " + codigoProducto);
    }

    producto.setNombre(productoRequestDTO.getNombre());
    producto.setCategoria(productoRequestDTO.getCategoria());
    producto.setMarca(productoRequestDTO.getMarca());
    producto.setPrecio(productoRequestDTO.getPrecio());
    producto.setStock(productoRequestDTO.getStock());

    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

  @Override
  public ProductoResponseDTO patchProducto(Long codigoProducto, ProductoUpdateRequestDTO productoUpdateRequestDTO) {
    Producto producto = productoRespository.findById(codigoProducto)
        .orElseThrow(() -> new NotFoundException("El Producto con Codigo " + codigoProducto + " no existe."));

    if (producto.getEstado() == EstadoProducto.DESCONTINUADO) {
      throw new BusinessException(
          "El Producto con Codigo " + codigoProducto + " se encuentra DESCONTINUADO por lo que no se puede editar");
    }

    if (productoUpdateRequestDTO.getNombre() == null && productoUpdateRequestDTO.getCategoria() == null
        && productoUpdateRequestDTO.getMarca() == null
        && productoUpdateRequestDTO.getPrecio() == null && productoUpdateRequestDTO.getStock() == null) {
      throw new BusinessException("No se enviaron campos para actualizar");
    }

    if (productoUpdateRequestDTO.getNombre() != null)
      producto.setNombre(productoUpdateRequestDTO.getNombre());
    if (productoUpdateRequestDTO.getCategoria() != null)
      producto.setCategoria(productoUpdateRequestDTO.getCategoria());
    if (productoUpdateRequestDTO.getMarca() != null)
      producto.setMarca(productoUpdateRequestDTO.getMarca());
    if (productoUpdateRequestDTO.getPrecio() != null)
      producto.setPrecio(productoUpdateRequestDTO.getPrecio());
    if (productoUpdateRequestDTO.getStock() != null)
      producto.setStock(productoUpdateRequestDTO.getStock());
    return Mapper.toResponseDTO(productoRespository.save(producto));
  }

}
