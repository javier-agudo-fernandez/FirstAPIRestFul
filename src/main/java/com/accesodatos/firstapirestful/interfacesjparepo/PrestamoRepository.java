package com.accesodatos.firstapirestful.interfacesjparepo;

import com.accesodatos.firstapirestful.modelo.Ejemplar;
import com.accesodatos.firstapirestful.modelo.Prestamo;
import com.accesodatos.firstapirestful.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo,Integer> {

}

