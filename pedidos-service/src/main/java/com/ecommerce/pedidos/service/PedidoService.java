package com.ecommerce.pedidos.service;

import com.ecommerce.pedidos.dto.DetallePedidoDTO;
import com.ecommerce.pedidos.dto.PedidoDTO;
import com.ecommerce.pedidos.dto.ProductoResponse;
import com.ecommerce.pedidos.model.DetallePedido;
import com.ecommerce.pedidos.model.EstadoPedido;
import com.ecommerce.pedidos.model.Pedido;
import com.ecommerce.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final WebClient webClientProductos;

    @Transactional
    public PedidoDTO crearPedido(PedidoDTO pedidoDTO, Long usuarioId) {
        // 1. Validar y obtener información de los productos
        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetalles()) {
            ProductoResponse producto = webClientProductos.get()
                    .uri("/{id}", detalleDTO.getProductoId())
                    .retrieve()
                    .onStatus(status -> status.value() == 404, clientResponse ->
                            Mono.error(new NoSuchElementException("Producto no encontrado con ID: " + detalleDTO.getProductoId())))
                    .bodyToMono(ProductoResponse.class)
                    .block();

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto ID: " + producto.getId());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
            totalPedido = totalPedido.add(subtotal);

            DetallePedido detalle = DetallePedido.builder()
                    .productoId(producto.getId())
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();
            detalles.add(detalle);
        }

        // 2. Crear el Pedido
        Pedido pedido = Pedido.builder()
                .usuarioId(usuarioId)
                .fecha(LocalDateTime.now())
                .total(totalPedido)
                .estado(EstadoPedido.CONFIRMADO)
                .direccionEnvio(pedidoDTO.getDireccionEnvio())
                .comunaEnvio(pedidoDTO.getComunaEnvio())
                .regionEnvio(pedidoDTO.getRegionEnvio())
                .detalles(detalles)
                .build();

        // Asignar el pedido a los detalles
        detalles.forEach(d -> d.setPedido(pedido));

        // 3. Guardar el Pedido
        pedido = pedidoRepository.save(pedido);

        // 4. Reducir el stock (Comunicación con Productos Service)
        for (DetallePedido detalle : detalles) {
            webClientProductos.put()
                    .uri("/{id}/reducir-stock?cantidad={cantidad}", detalle.getProductoId(), detalle.getCantidad())
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse ->
                            Mono.error(new RuntimeException("Error al reducir stock para producto ID: " + detalle.getProductoId())))
                    .bodyToMono(Void.class)
                    .block();
        }

        return toDTO(pedido);
    }

    // Listar pedidos del usuario autenticado (Paginación)
    public Page<PedidoDTO> listarMisPedidos(Long usuarioId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pedidoRepository.findByUsuarioId(usuarioId, pageable).map(this::toDTO);
    }

    // Obtener un pedido por ID (solo si pertenece al usuario)
    public PedidoDTO obtenerPedidoPorId(Long id, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado con ID: " + id));

        if (!pedido.getUsuarioId().equals(usuarioId)) {
            throw new SecurityException("Acceso denegado. El pedido no pertenece al usuario.");
        }

        return toDTO(pedido);
    }

    // Listar todos los pedidos (ADMIN)
    public Page<PedidoDTO> listarTodosLosPedidos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pedidoRepository.findAll(pageable).map(this::toDTO);
    }

    // Actualizar estado del pedido (ADMIN)
    @Transactional
    public PedidoDTO actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado con ID: " + id));

        pedido.setEstado(nuevoEstado);
        pedido = pedidoRepository.save(pedido);
        return toDTO(pedido);
    }

    private PedidoDTO toDTO(Pedido pedido) {
        List<DetallePedidoDTO> detalleDTOs = pedido.getDetalles().stream()
                .map(d -> DetallePedidoDTO.builder()
                        .productoId(d.getProductoId())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal())
                        .build())
                .toList();

        return PedidoDTO.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .fecha(pedido.getFecha())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .direccionEnvio(pedido.getDireccionEnvio())
                .comunaEnvio(pedido.getComunaEnvio())
                .regionEnvio(pedido.getRegionEnvio())
                .detalles(detalleDTOs)
                .build();
    }
}
