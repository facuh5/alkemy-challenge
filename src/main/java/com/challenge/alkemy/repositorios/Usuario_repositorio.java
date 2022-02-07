/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.repositorios;

import com.challenge.alkemy.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author usuario
 */
@Repository
public interface Usuario_repositorio extends JpaRepository<Usuario, String>{
    
    @Query("select u from Usuario u where u.nombre = :nombre")
    public Optional<Usuario> findByNombre(@Param("nombre") String nombre);
    
    @Query("select u from Usuario u where u.email = :email")
    public Usuario findByEmail(@Param("email") String email);
}
