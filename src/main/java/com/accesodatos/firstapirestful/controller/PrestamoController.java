package com.accesodatos.firstapirestful.controller;

import com.accesodatos.firstapirestful.interfacesjparepo.EjemplarRepository;
import com.accesodatos.firstapirestful.interfacesjparepo.PrestamoRepository;
import com.accesodatos.firstapirestful.interfacesjparepo.UsuarioRepository;
import com.accesodatos.firstapirestful.modelo.Prestamo;
import com.accesodatos.firstapirestful.modelo.Usuario;
import com.accesodatos.firstapirestful.modelo.Ejemplar;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping
    public ResponseEntity<List<Prestamo>> getPrestamos() {
        return ResponseEntity.ok(prestamoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> getPrestamoById(@PathVariable Integer id) {
        return prestamoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prestamo> addPrestamo(@RequestBody Prestamo prestamo) {
        return ResponseEntity.ok(prestamoRepository.save(prestamo));
    }
    @PostMapping(value = "/addPrestamo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> addPrestamoForm(
            @RequestParam int usuarioId,
            @RequestParam int ejemplarId,
            @RequestParam String fechaInicio,
            @RequestParam(required = false) String fechaDevolucion) {

        try {
            // Convertir fechas de String a LocalDate
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

            // Buscar usuario y ejemplar en la base de datos
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
            return ResponseEntity.ok(prestamoGuardado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }





    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> updatePrestamo(@PathVariable Integer id, @RequestBody Prestamo prestamo) {
        if (!prestamoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        prestamo.setId(id);
        return ResponseEntity.ok(prestamoRepository.save(prestamo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Integer id) {
        if (!prestamoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        prestamoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}