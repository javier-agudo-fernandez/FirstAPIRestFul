package com.accesodatos.firstapirestful.controller;

import com.accesodatos.firstapirestful.interfacesjparepo.EjemplarRepository;
import com.accesodatos.firstapirestful.interfacesjparepo.LibroRepository;
import com.accesodatos.firstapirestful.modelo.Ejemplar;
import com.accesodatos.firstapirestful.modelo.Libro;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ejemplares")
public class EjemplarController {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private LibroRepository libroRepository;

    // Obtener todos los ejemplares
    @GetMapping
    public ResponseEntity<List<Ejemplar>> getEjemplares() {
        List<Ejemplar> ejemplares = ejemplarRepository.findAll();
        if (ejemplares.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay ejemplares disponibles");
        }
        return ResponseEntity.ok(ejemplares);
    }

    // Obtener ejemplar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ejemplar> getEjemplarById(@PathVariable Integer id) {
        return ejemplarRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado"));
    }

    // Crear un nuevo ejemplar
    @PostMapping
    public ResponseEntity<Ejemplar> addEjemplar(@Valid @RequestBody Ejemplar ejemplar) {
        if (ejemplar.getLibro() == null || ejemplar.getLibro().getIsbn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ejemplar debe estar asociado a un libro con ISBN");
        }


        Libro libro = libroRepository.findById(ejemplar.getLibro().getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        ejemplar.setLibro(libro);
        Ejemplar savedEjemplar = ejemplarRepository.save(ejemplar);

        return ResponseEntity.created(URI.create("/ejemplares/" + savedEjemplar.getId())).body(savedEjemplar);
    }

    // Crear un ejemplar desde formulario (Multipart Form)
    @PostMapping(value = "/EjemplarForm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ejemplar> addEjemplarForm(@RequestParam String isbn, @RequestParam String estado) {
        Libro libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        Ejemplar ejemplarToPost = new Ejemplar();
        ejemplarToPost.setLibro(libro);
        ejemplarToPost.setEstado(estado);

        Ejemplar savedEjemplar = ejemplarRepository.save(ejemplarToPost);

        return ResponseEntity.created(URI.create("/ejemplares/" + savedEjemplar.getId())).body(savedEjemplar);
    }

    // Actualizar un ejemplar
    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> updateEjemplar(@PathVariable Integer id, @RequestBody Ejemplar ejemplar) {
        if (ejemplar.getLibro() == null || ejemplar.getLibro().getIsbn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ejemplar debe tener un libro asociado con ISBN");
        }

        return ejemplarRepository.findById(id).map(existingEjemplar -> {
            Libro libro = libroRepository.findById(ejemplar.getLibro().getIsbn())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

            existingEjemplar.setLibro(libro);
            existingEjemplar.setEstado(ejemplar.getEstado());

            Ejemplar updatedEjemplar = ejemplarRepository.save(existingEjemplar);
            return ResponseEntity.ok(updatedEjemplar);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado"));
    }

    // Eliminar un ejemplar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjemplar(@PathVariable Integer id) {
        if (!ejemplarRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado");
        }
        ejemplarRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
