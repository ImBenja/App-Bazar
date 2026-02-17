package com.bazar.app.service;

import java.math.BigDecimal;
import java.util.List;

import com.bazar.app.dto.requests.create.ProductoCreateRequestDTO;
import com.bazar.app.dto.requests.update.ProductoUpdateRequestDTO;
import com.bazar.app.dto.responses.ProductoResponseDTO;
import com.bazar.app.enums.EstadoProducto;

public interface IProductoService {

  List<ProductoResponseDTO> getProductos();

  List<ProductoResponseDTO> getProductosConStockBajo();

  List<ProductoResponseDTO> getProductosByNombre(String nombre);

  List<ProductoResponseDTO> getProductosByMarca(String marca);

  List<ProductoResponseDTO> getProductosByCategoria(String categoria);

  List<ProductoResponseDTO> getProductosByEstado(EstadoProducto estado);

  List<ProductoResponseDTO> getProductosByPrecioMenorA(BigDecimal precio);

  List<ProductoResponseDTO> getProductosByPrecioMayorA(BigDecimal precio);

  List<ProductoResponseDTO> getProductosByRangoDePrecio(BigDecimal min, BigDecimal max);

  ProductoResponseDTO getProductoByCodigo(Long codigoProducto);

  ProductoResponseDTO createProducto(ProductoCreateRequestDTO productoRequestDTO);

  ProductoResponseDTO updateProducto(Long codigoProducto, ProductoCreateRequestDTO productoRequestDTO);

  ProductoResponseDTO patchProducto(Long codigoProducto, ProductoUpdateRequestDTO productoUpdateRequestDTO);

  ProductoResponseDTO activarProducto(Long codigoProducto);

  ProductoResponseDTO inactivarProducto(Long codigoProducto);

  ProductoResponseDTO marcarAgotado(Long codigoProducto);

  ProductoResponseDTO descontinuarProducto(Long codigoProducto);
}
