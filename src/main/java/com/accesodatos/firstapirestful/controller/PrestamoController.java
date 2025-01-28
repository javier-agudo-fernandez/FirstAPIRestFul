package com.accesodatos.firstapirestful.controller;

import com.accesodatos.firstapirestful.interfacesjparepo.EjemplarRepository;
import com.accesodatos.firstapirestful.interfacesjparepo.PrestamoRepository;
import com.accesodatos.firstapirestful.interfacesjparepo.UsuarioRepository;
import com.accesodatos.firstapirestful.modelo.Prestamo;
import com.accesodatos.firstapirestful.modelo.Usuario;
import com.accesodatos.firstapirestful.modelo.Ejemplar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EjemplarRepository ejemplarRepository;

    // Obtener todos los préstamos
    @GetMapping
    public ResponseEntity<List<Prestamo>> getPrestamos() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        if (prestamos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay préstamos disponibles.");
        }
        return ResponseEntity.ok(prestamos);
    }

    // Obtener préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> getPrestamoById(@PathVariable Integer id) {
        return prestamoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado"));
    }

    // Crear un préstamo con JSON
    @PostMapping
    public ResponseEntity<Prestamo> addPrestamo(@RequestBody Prestamo prestamo) {
        if (prestamo.getUsuario() == null || prestamo.getEjemplar() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario y ejemplar son obligatorios.");
        }

        if (!usuarioRepository.existsById(prestamo.getUsuario().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }

        if (!ejemplarRepository.existsById(prestamo.getEjemplar().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado.");
        }

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoGuardado);
    }

    // Crear préstamo desde formulario
    @PostMapping(value = "/addPrestamo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> addPrestamoForm(
            @RequestParam int usuarioId,
            @RequestParam int ejemplarId,
            @RequestParam String fechaInicio,
            @RequestParam(required = false) String fechaDevolucion) {

        // Convertir fechas y validar formato
        LocalDate fechaInicioParsed;
        try {
            fechaInicioParsed = LocalDate.parse(fechaInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato inválido para fechaInicio. Use 'yyyy-MM-dd'.");
        }

        LocalDate fechaDevolucionParsed = null;
        if (fechaDevolucion != null && !fechaDevolucion.isEmpty()) {
            try {
                fechaDevolucionParsed = LocalDate.parse(fechaDevolucion, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Formato inválido para fechaDevolucion. Use 'yyyy-MM-dd'.");
            }
        }

        // Validar usuario y ejemplar
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado"));

        // Crear y guardar el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(fechaInicioParsed);
        prestamo.setFechaDevolucion(fechaDevolucionParsed);

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoGuardado);
    }

    // Actualizar un préstamo
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> updatePrestamo(@PathVariable Integer id, @RequestBody Prestamo prestamo) {
        if (!prestamoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado");
        }

        prestamo.setId(id);
        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        return ResponseEntity.ok(prestamoActualizado);
    }

    // Eliminar un préstamo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Integer id) {
        if (!prestamoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado");
        }

        prestamoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
