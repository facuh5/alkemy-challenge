/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.repositorios;
import com.challenge.alkemy.entidades.Personaje;
import java.util.List;
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
public interface Personaje_repositorio extends JpaRepository<Personaje, String>{
    
    @Query("select p from Personaje p where p.nombre =: nombre")
    public Optional<Personaje> findByNombre (@Param("nombre") String nombre);
    
    @Query("select p from Personaje p where p.edad =: edad")
    public Optional<List<Personaje>> findByEdad (@Param("edad") int edad);
    
    @Query("select p from Personaje p where p.peso =: peso")
    public Optional<List<Personaje>> findByPeso (@Param("peso") double peso);
}
