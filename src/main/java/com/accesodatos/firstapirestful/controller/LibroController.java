package com.accesodatos.firstapirestful.controller;

import com.accesodatos.firstapirestful.interfacesjparepo.LibroRepository;
import com.accesodatos.firstapirestful.modelo.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository repositorioLibros;

    // GET --> Obtener todos los libros
    @GetMapping
    public ResponseEntity<List<Libro>> getLibro() {
        List<Libro> lista = repositorioLibros.findAll();
        if (lista.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay libros disponibles");
        }
        return ResponseEntity.ok(lista);
    }

    // GET BY ISBN --> Obtener un libro por ISBN
    @GetMapping("/{isbn}")
    public ResponseEntity<Libro> getLibroJson(@PathVariable String isbn) {
        return repositorioLibros.findById(isbn)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    // POST --> Insertar un nuevo libro
    @PostMapping
    public ResponseEntity<Libro> addLibro(@RequestBody Libro libro) {
        if (repositorioLibros.existsById(libro.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El ISBN ya está registrado");
        }
        Libro libroPersistido = repositorioLibros.save(libro);
        return ResponseEntity.created(URI.create("/libros/" + libroPersistido.getIsbn())).body(libroPersistido);
    }

    // POST con Form normal
    @PostMapping(value = "/libroForm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Libro> addLibroForm(@RequestParam String isbn,
                                              @RequestParam String titulo,
                                              @RequestParam String autor) {
        if (repositorioLibros.existsById(isbn)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El ISBN ya está registrado");
        }

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        Libro libroPersistido = repositorioLibros.save(libro);

        return ResponseEntity.created(URI.create("/libros/" + libroPersistido.getIsbn())).body(libroPersistido);
    }

    // POST con Form y archivo
    @PostMapping(value = "/libroFormFichero", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Libro> addLibroFormFichero(@RequestParam String isbn,
                                                     @RequestParam String titulo,
                                                     @RequestParam String autor,
                                                     @RequestParam MultipartFile imagen) {
        if (repositorioLibros.existsById(isbn)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El ISBN ya está registrado");
        }

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        repositorioLibros.save(libro);

        return ResponseEntity.created(URI.create("/libros/" + libro.getIsbn())).body(libro);
    }

    // PUT --> Actualizar libro
    @PutMapping("/{isbn}")
    public ResponseEntity<Libro> updateLibro(@PathVariable String isbn, @RequestBody Libro libro) {
        if (!repositorioLibros.existsById(isbn)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        libro.setIsbn(isbn); // Asegurar que se mantiene el mismo ISBN
        Libro libroActualizado = repositorioLibros.save(libro);

        return ResponseEntity.ok(libroActualizado);
    }

    // DELETE --> Eliminar libro
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteLibro(@PathVariable String isbn) {
        if (!repositorioLibros.existsById(isbn)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        repositorioLibros.deleteById(isbn);
        return ResponseEntity.noContent().build();
    }
}
