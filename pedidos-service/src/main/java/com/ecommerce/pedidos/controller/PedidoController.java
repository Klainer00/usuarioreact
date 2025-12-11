package com.ecommerce.pedidos.controller;

import com.ecommerce.pedidos.dto.PedidoDTO;
import com.ecommerce.pedidos.model.EstadoPedido;
import com.ecommerce.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // POST /pedidos
    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO,
                                                 @RequestHeader("X-User-ID") long usuarioId) {
        return new ResponseEntity<>(pedidoService.crearPedido(pedidoDTO, usuarioId), HttpStatus.CREATED);
    }

    // GET /pedidos (mis pedidos)
    @GetMapping
    public ResponseEntity<Page<PedidoDTO>> listarMisPedidos(
            @RequestHeader("X-User-ID") long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PedidoDTO> pedidos = pedidoService.listarMisPedidos(usuarioId, page, size);
        return ResponseEntity.ok(pedidos);
    }

    // GET /pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(
            @PathVariable long id,
            @RequestHeader("X-User-ID") long usuarioId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id, usuarioId));
    }

    // Endpoints de Administración

    // GET /admin/pedidos (ADMIN)
    @GetMapping("/admin")
    public ResponseEntity<Page<PedidoDTO>> listarTodosLosPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PedidoDTO> pedidos = pedidoService.listarTodosLosPedidos(page, size);
        return ResponseEntity.ok(pedidos);
    }

    // PUT /admin/pedidos/{id}/estado (ADMIN)
    @PutMapping("/admin/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstadoPedido(
            @PathVariable long id,
            @RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, estado));
    }
}
