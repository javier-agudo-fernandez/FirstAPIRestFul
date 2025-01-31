package com.accesodatos.firstapirestful.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @Size(max = 20)
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "ISBN debe ser un formato válido")
    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Size(max = 200)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "El título solo puede contener caracteres alfanuméricos")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Size(max = 100)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "El título solo puede contener caracteres alfanuméricos")
    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    // Relación uno a muchos con Ejemplar
    @JsonIncludeProperties({"id", "estado"})
    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private Set<Ejemplar> ejemplares = new LinkedHashSet<>();  // Cambié el nombre a ejemplares (plural)

    // Getters y Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Set<Ejemplar> getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Set<Ejemplar> ejemplares) {
        this.ejemplares = ejemplares;
    }
}
