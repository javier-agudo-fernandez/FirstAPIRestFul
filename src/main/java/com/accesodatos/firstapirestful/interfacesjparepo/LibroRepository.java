package com.accesodatos.firstapirestful.interfacesjparepo;

import com.accesodatos.firstapirestful.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro,String> {

}

