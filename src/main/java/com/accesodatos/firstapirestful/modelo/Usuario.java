package com.accesodatos.firstapirestful.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 15)
    @NotNull
    @Column(name = "dni", nullable = false, length = 15)
    private String dni;

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "El nombre solo puede contener caracteres alfanuméricos")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank
    @Pattern(regexp = "normal|administrador", message = "Tipo debe ser uno de los siguientes valores: normal, administrador")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]{1,50}@gmail\\.com", message = "Email debe ser un correo de Gmail válido")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 4, max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password debe ser una cadena alfanumérica de entre 4 y 12 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "penalizacion_hasta")
    private LocalDate penalizacionHasta;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getPenalizacionHasta() {
        return penalizacionHasta;
    }

    public void setPenalizacionHasta(LocalDate penalizacionHasta) {
        this.penalizacionHasta = penalizacionHasta;
    }
}