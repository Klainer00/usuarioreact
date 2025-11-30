package controller;

import lombok.RequiredArgsConstructor;
import modelo.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dto.RegisterRequestDto;
import dto.LoginRequestDto;
import dto.UsuarioResponseDto;
import dto.LoginResponseDto;
import service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<dto.UsuarioResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(requestDto); 
            
            dto.UsuarioResponseDto responseDto = dto.UsuarioResponseDto.builder()
                .id(nuevoUsuario.getId())
                .nombre(nuevoUsuario.getNombre())
                .correo(nuevoUsuario.getCorreo())
                .direccion(nuevoUsuario.getDireccion())
                .telefono(nuevoUsuario.getTelefono())
                .rol(nuevoUsuario.getRole().getNombre())
                .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            System.err.println("Error en registro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            Usuario usuario = usuarioService.loginUsuario(requestDto);
            
            LoginResponseDto responseDto = LoginResponseDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRole().getNombre())
                .token("token-" + usuario.getId()) // Token simple (implementar JWT después)
                .build();
            
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}