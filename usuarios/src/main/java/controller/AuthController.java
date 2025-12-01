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
import dto.LoginResponseDto;
import service.UsuarioService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(requestDto); 
            
            dto.UsuarioResponseDto responseDto = dto.UsuarioResponseDto.builder()
                .id(nuevoUsuario.getId())
                .rut(nuevoUsuario.getRut())
                .nombre(nuevoUsuario.getNombre())
                .apellido(nuevoUsuario.getApellido())
                .email(nuevoUsuario.getEmail())
                .direccion(nuevoUsuario.getDireccion())
                .region(nuevoUsuario.getRegion())
                .comuna(nuevoUsuario.getComuna())
                .fechaNacimiento(nuevoUsuario.getFechaNacimiento())
                .estado(nuevoUsuario.getEstado())
                .rol(nuevoUsuario.getRol().getNombre())
                .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            
        } catch (IllegalArgumentException e) {
            dto.ErrorResponseDto errorDto = dto.ErrorResponseDto.builder()
                .mensaje(e.getMessage())
                .codigo("CONFLICT")
                .status(409)
                .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
        } catch (Exception e) {
            System.err.println("Error en registro: " + e.getMessage());
            e.printStackTrace();
            dto.ErrorResponseDto errorDto = dto.ErrorResponseDto.builder()
                .mensaje("Error interno del servidor: " + e.getMessage())
                .codigo("INTERNAL_ERROR")
                .status(500)
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        try {
            Usuario usuario = usuarioService.loginUsuario(requestDto);
            
            LoginResponseDto responseDto = LoginResponseDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().getNombre())
                .token("token-" + usuario.getId()) // Token simple (implementar JWT después)
                .build();
            
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
            
        } catch (RuntimeException e) {
            dto.ErrorResponseDto errorDto = dto.ErrorResponseDto.builder()
                .mensaje("Credenciales inválidas: " + e.getMessage())
                .codigo("UNAUTHORIZED")
                .status(401)
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            dto.ErrorResponseDto errorDto = dto.ErrorResponseDto.builder()
                .mensaje("Error interno del servidor: " + e.getMessage())
                .codigo("INTERNAL_ERROR")
                .status(500)
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
        }
    }
}