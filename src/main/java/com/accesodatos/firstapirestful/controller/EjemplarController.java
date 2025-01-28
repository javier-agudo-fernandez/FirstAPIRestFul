package com.accesodatos.firstapirestful.controller;

import com.accesodatos.firstapirestful.interfacesjparepo.EjemplarRepository;
import com.accesodatos.firstapirestful.interfacesjparepo.LibroRepository;
import com.accesodatos.firstapirestful.modelo.Ejemplar;
import com.accesodatos.firstapirestful.modelo.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/ejemplares")
public class EjemplarController {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping
    public ResponseEntity<List<Ejemplar>> getEjemplares() {
        return ResponseEntity.ok(ejemplarRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejemplar> getEjemplarById(@PathVariable Integer id) {
        return ejemplarRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ejemplar> addEjemplar(@RequestBody Ejemplar ejemplar) {
        // Buscar el libro por ISBN
        Libro libro = libroRepository.findById(ejemplar.getLibro().getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        // Asociar el libro al ejemplar
        ejemplar.setLibro(libro);

        // Guardar el ejemplar (Spring JPA se encarga de asociar el libro automáticamente)
        Ejemplar savedEjemplar = ejemplarRepository.save(ejemplar);

        return ResponseEntity.ok(savedEjemplar);
    }

    @PostMapping(value = "/EjemplarForm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ejemplar> addEjemplarForm(@RequestParam String isbn,
                                                    @RequestParam String estado){
        Libro libro = this.libroRepository.findById(isbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        Ejemplar ejemplarToPost = new Ejemplar();
        ejemplarToPost.setLibro(libro);
        ejemplarToPost.setEstado(estado);
        this.ejemplarRepository.save(ejemplarToPost);
        return ResponseEntity.created(null).body(ejemplarToPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> updateEjemplar(@PathVariable Integer id, @RequestBody Ejemplar ejemplar) {
        if (!ejemplarRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ejemplar.setId(id);
        return ResponseEntity.ok(ejemplarRepository.save(ejemplar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjemplar(@PathVariable Integer id) {
        if (!ejemplarRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ejemplarRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}