package com.accesodatos.firstapirestful.interfacesjparepo;

import com.accesodatos.firstapirestful.modelo.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjemplarRepository extends JpaRepository<Ejemplar,Integer> {

}