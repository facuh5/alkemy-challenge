/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.repositorios;

import com.challenge.alkemy.entidades.Genero;
import com.challenge.alkemy.entidades.PeliculaSerie;
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
public interface Pelicula_o_Serie_repositorio extends JpaRepository<PeliculaSerie, String>{
    
    @Query("select pos from PeliculaSerie pos where pos.titulo =: titulo")
    public Optional<PeliculaSerie> findByTitulo (@Param("titulo") String titulo);
    
//    @Query("select pos from PeliculaSerie pos where pos.genero =: genero")
//    public List<PeliculaSerie> findByGenero (@Param("genero") Genero genero);
}
