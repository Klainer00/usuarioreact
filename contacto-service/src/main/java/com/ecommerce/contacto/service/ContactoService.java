package com.ecommerce.contacto.service;

import com.ecommerce.contacto.dto.ContactoDTO;
import com.ecommerce.contacto.model.Contacto;
import com.ecommerce.contacto.repository.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final ContactoRepository contactoRepository;

    // Crear mensaje de contacto
    @Transactional
    public ContactoDTO crearMensaje(ContactoDTO dto) {
        Contacto contacto = Contacto.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .mensaje(dto.getMensaje())
                .atendido(false)
                .build();
        contacto = contactoRepository.save(contacto);
        return toDTO(contacto);
    }

    // Listar todos los mensajes (ADMIN)
    public Page<ContactoDTO> listarMensajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return contactoRepository.findAll(pageable).map(this::toDTO);
    }

    // Marcar mensaje como atendido (ADMIN)
    @Transactional
    public ContactoDTO marcarComoAtendido(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mensaje de contacto no encontrado con ID: " + id));

        contacto.setAtendido(true);
        contacto = contactoRepository.save(contacto);
        return toDTO(contacto);
    }

    private ContactoDTO toDTO(Contacto contacto) {
        return ContactoDTO.builder()
                .id(contacto.getId())
                .nombre(contacto.getNombre())
                .email(contacto.getEmail())
                .telefono(contacto.getTelefono())
                .mensaje(contacto.getMensaje())
                .fecha(contacto.getFecha())
                .atendido(contacto.getAtendido())
                .build();
    }
}
