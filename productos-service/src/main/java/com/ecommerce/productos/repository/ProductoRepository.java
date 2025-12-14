package com.ecommerce.productos.repository;

import com.ecommerce.productos.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Búsqueda y filtrado con paginación
    @Query("""
        SELECT p FROM Producto p
        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        AND (:categoria IS NULL OR LOWER(p.categoria.nombre) = LOWER(:categoria))
        AND p.activo = true
    """)
    Page<Producto> buscarProductos(
            @Param("nombre") String nombre,
            @Param("categoria") String categoria,
            Pageable pageable
    );

    // Gestión de inventario (reducir stock)
    @Modifying
    @Transactional
    @Query("""
        UPDATE Producto p
        SET p.stock = p.stock - :cantidad
        WHERE p.id = :id
        AND p.stock >= :cantidad
    """)
    int actualizarStock(
            @Param("id") Long id,
            @Param("cantidad") Integer cantidad
    );
}
