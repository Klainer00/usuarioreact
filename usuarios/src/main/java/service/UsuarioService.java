package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import dto.RegisterRequestDto; 
import dto.LoginRequestDto; // Importar LoginRequestDto

import modelo.Usuario;
import modelo.RolEntity; 
import repository.UsuarioRepository;
import repository.RolRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(RegisterRequestDto requestDto) {
        if (usuarioRepository.findByCorreo(requestDto.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está en uso"); 
        }

        Usuario usuario = new Usuario();
        
        usuario.setNombre(requestDto.getNombre()); 
        usuario.setCorreo(requestDto.getCorreo());
        
        usuario.setContraseña(passwordEncoder.encode(requestDto.getContraseña()));
        usuario.setDireccion(requestDto.getDireccion());
        usuario.setTelefono(requestDto.getTelefono());

        // Cambiar "USER" por "USUARIO"
        String rolNombre = "USUARIO"; 
        RolEntity rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Error: Rol '" + rolNombre + "' no encontrado. " +
                                                        "Asegúrate de que el rol exista en la tabla 'roles'."));
        
        usuario.setRole(rol);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> actualizarUsuario(Long id, Usuario usuarioDetalles) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuarioExistente = usuarioOpt.get();
        
        usuarioExistente.setNombre(usuarioDetalles.getNombre());
        usuarioExistente.setCorreo(usuarioDetalles.getCorreo());
        
        if (usuarioDetalles.getContraseña() != null && !usuarioDetalles.getContraseña().isEmpty()) {
            usuarioExistente.setContraseña(passwordEncoder.encode(usuarioDetalles.getContraseña()));
        }
        
        usuarioExistente.setDireccion(usuarioDetalles.getDireccion());
        usuarioExistente.setTelefono(usuarioDetalles.getTelefono());

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
        Usuario usuario = usuarioRepository.findByCorreo(requestDto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));
        
        if (!passwordEncoder.matches(requestDto.getContraseña(), usuario.getContraseña())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }
        
        return usuario;
    }
}