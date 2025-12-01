package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import dto.RegisterRequestDto; 
import dto.LoginRequestDto;

import modelo.Usuario;
import modelo.RolUsuario; 
import repository.UsuarioRepository;
import repository.RolUsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(RegisterRequestDto requestDto) {
        // Usar email o correo (compatibilidad)
        String email = requestDto.getEmail() != null ? requestDto.getEmail() : requestDto.getCorreo();
        
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso"); 
        }
        
        // Verificar si el RUT ya existe
        Optional<Usuario> usuarioPorRut = usuarioRepository.findByRut(requestDto.getRut());
        if (usuarioPorRut.isPresent()) {
            throw new IllegalArgumentException("El RUT ya está registrado");
        }

        Usuario usuario = new Usuario();
        
        usuario.setRut(requestDto.getRut());
        usuario.setNombre(requestDto.getNombre()); 
        usuario.setApellido(requestDto.getApellido());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(requestDto.getContraseña()));
        usuario.setDireccion(requestDto.getDireccion());
        usuario.setRegion(requestDto.getRegion());
        usuario.setComuna(requestDto.getComuna());
        usuario.setFechaNacimiento(requestDto.getFechaNacimiento());
        usuario.setEstado("ACTIVO");

        // Asignar rol por defecto
        String rolNombre = "USUARIO"; 
        RolUsuario rol = rolUsuarioRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new IllegalArgumentException("Error: Rol '" + rolNombre + "' no encontrado. " +
                                                        "Asegúrate de que el rol exista en la tabla 'roles'."));
        
        usuario.setRol(rol);
        
        try {
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("Usuario registrado exitosamente: " + usuarioGuardado.getEmail());
            return usuarioGuardado;
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            throw new RuntimeException("Error al registrar el usuario", e);
        }
    }

    public Optional<Usuario> actualizarUsuario(Long id, Usuario usuarioDetalles) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuarioExistente = usuarioOpt.get();
        
        usuarioExistente.setNombre(usuarioDetalles.getNombre());
        usuarioExistente.setApellido(usuarioDetalles.getApellido());
        usuarioExistente.setEmail(usuarioDetalles.getEmail());
        
        if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioDetalles.getPassword()));
        }
        
        usuarioExistente.setDireccion(usuarioDetalles.getDireccion());
        usuarioExistente.setRegion(usuarioDetalles.getRegion());
        usuarioExistente.setComuna(usuarioDetalles.getComuna());
        usuarioExistente.setEstado(usuarioDetalles.getEstado());

        return Optional.of(usuarioRepository.save(usuarioExistente));
    }


    public boolean borrarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false; 
    }

    public Usuario loginUsuario(LoginRequestDto requestDto) {
        // Intentar con email o correo
        String email = requestDto.getEmail() != null ? requestDto.getEmail() : requestDto.getCorreo();
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));
        
        if (!passwordEncoder.matches(requestDto.getContraseña(), usuario.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        
        return usuario;
    }
}