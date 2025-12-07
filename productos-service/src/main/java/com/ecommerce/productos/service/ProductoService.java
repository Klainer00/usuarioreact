package com.ecommerce.productos.service;

import com.ecommerce.productos.dto.ProductoDTO;
import com.ecommerce.productos.model.Producto;
import com.ecommerce.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // CRUD - Crear
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .categoria(dto.getCategoria())
                .imagenUrl(dto.getImagenUrl())
                .activo(true)
                .build();
        producto = productoRepository.save(producto);
        return toDTO(producto);
    }

    // CRUD - Leer (Paginación, Búsqueda y Filtrado)
    public Page<ProductoDTO> listarProductos(String nombre, String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productos = productoRepository.buscarProductos(nombre, categoria, pageable);
        return productos.map(this::toDTO);
    }

    // CRUD - Leer por ID
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + id));
        return toDTO(producto);
    }

    // CRUD - Actualizar
    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + id));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : producto.getActivo());

        producto = productoRepository.save(producto);
        return toDTO(producto);
    }

    // CRUD - Eliminar (Desactivar)
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + id));
        producto.setActivo(false); // Eliminación lógica
        productoRepository.save(producto);
    }

    // Gestión de Inventario (para ser usado por Pedidos Service)
    @Transactional
    public void reducirStock(Long id, Integer cantidad) {
        int updatedRows = productoRepository.actualizarStock(id, cantidad);
        if (updatedRows == 0) {
            throw new RuntimeException("No se pudo reducir el stock del producto " + id + ". Stock insuficiente o producto no encontrado.");
        }
    }

    private ProductoDTO toDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .categoria(producto.getCategoria())
                .imagenUrl(producto.getImagenUrl())
                .activo(producto.getActivo())
                .build();
    }
}
