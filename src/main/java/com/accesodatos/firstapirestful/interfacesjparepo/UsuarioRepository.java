package com.accesodatos.firstapirestful.interfacesjparepo;

import com.accesodatos.firstapirestful.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

}