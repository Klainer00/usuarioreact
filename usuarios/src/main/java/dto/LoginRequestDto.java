package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestDto {
    private String correo;
    private String email; // Usar email en lugar de correo
    
    @JsonProperty("contraseña")
    private String contraseña;
}
