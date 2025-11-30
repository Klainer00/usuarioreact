package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDto {
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private String region;
    private String comuna;
    private String fechaNacimiento;
    private String rol;
    private String estado;
}