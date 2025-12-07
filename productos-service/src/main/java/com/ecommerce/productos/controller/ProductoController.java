package com.ecommerce.productos.controller;

import com.ecommerce.productos.dto.ProductoDTO;
import com.ecommerce.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // GET /productos (Paginación, Búsqueda y Filtrado)
    @GetMapping
    public ResponseEntity<Page<ProductoDTO>> listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductoDTO> productos = productoService.listarProductos(nombre, categoria, page, size);
        return ResponseEntity.ok(productos);
    }

    // GET /productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    // POST /productos (ADMIN)
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        return new ResponseEntity<>(productoService.crearProducto(productoDTO), HttpStatus.CREATED);
    }

    // PUT /productos/{id} (ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    // DELETE /productos/{id} (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint interno para la gestión de inventario (solo para uso de otros microservicios)
    @PutMapping("/{id}/reducir-stock")
    public ResponseEntity<Void> reducirStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        productoService.reducirStock(id, cantidad);
        return ResponseEntity.ok().build();
    }
}
