package com.ecommerce.contacto.controller;

import com.ecommerce.contacto.dto.ContactoDTO;
import com.ecommerce.contacto.service.ContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    // POST /contacto
    @PostMapping
    public ResponseEntity<ContactoDTO> crearMensaje(@Valid @RequestBody ContactoDTO contactoDTO) {
        return new ResponseEntity<>(contactoService.crearMensaje(contactoDTO), HttpStatus.CREATED);
    }

    // GET /admin/contactos (ADMIN)
    @GetMapping("/admin")
    public ResponseEntity<Page<ContactoDTO>> listarMensajes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ContactoDTO> mensajes = contactoService.listarMensajes(page, size);
        return ResponseEntity.ok(mensajes);
    }

    // PUT /admin/contactos/{id}/atender (ADMIN)
    @PutMapping("/admin/{id}/atender")
    public ResponseEntity<ContactoDTO> marcarComoAtendido(@PathVariable Long id) {
        return ResponseEntity.ok(contactoService.marcarComoAtendido(id));
    }
}
