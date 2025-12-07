package com.ecommerce.contacto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(min = 10, max = 500, message = "El mensaje debe tener entre 10 y 500 caracteres")
    private String mensaje;

    private LocalDateTime fecha;
    private Boolean atendido;
}
