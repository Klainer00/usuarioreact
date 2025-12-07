package com.ecommerce.contacto.repository;

import com.ecommerce.contacto.model.Contacto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    Page<Contacto> findAll(Pageable pageable);
}
