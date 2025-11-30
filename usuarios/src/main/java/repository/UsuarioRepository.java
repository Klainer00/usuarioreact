package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import modelo.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario , Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findById(Long id);
}