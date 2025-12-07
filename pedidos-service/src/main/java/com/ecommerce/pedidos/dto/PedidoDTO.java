package com.ecommerce.pedidos.dto;

import com.ecommerce.pedidos.model.EstadoPedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long id;
    private Long usuarioId;
    private LocalDateTime fecha;
    private BigDecimal total;
    private EstadoPedido estado;

    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionEnvio;

    private String comunaEnvio;
    private String regionEnvio;

    @NotEmpty(message = "El pedido debe contener al menos un detalle")
    private List<DetallePedidoDTO> detalles;
}
