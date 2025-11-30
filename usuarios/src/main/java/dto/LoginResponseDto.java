package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private Long id;
    private String nombre;
    private String email;
    private String correo; // Para compatibilidad
    private String rol;
    private String token;
}
