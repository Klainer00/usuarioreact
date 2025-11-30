package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequestDto {
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String correo; // Para compatibilidad
    
    @JsonProperty("contraseña")
    private String contraseña;
    
    private String direccion;
    private String region;
    private String comuna;
    private String fechaNacimiento;
}