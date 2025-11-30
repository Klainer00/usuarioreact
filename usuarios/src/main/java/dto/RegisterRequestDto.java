package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequestDto {
    private String nombre;
    private String correo;
    
    @JsonProperty("contraseña")
    private String contraseña;
    
    private String telefono;
    private String direccion;
}