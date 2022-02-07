/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.servicios;

import com.challenge.alkemy.entidades.Imagen;
import com.challenge.alkemy.entidades.PeliculaSerie;
import com.challenge.alkemy.entidades.Personaje;
import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.repositorios.Pelicula_o_Serie_repositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author usuario
 */
@Service
public class Pelicula_o_Serie_servicio {
    @Autowired
    private Pelicula_o_Serie_repositorio posRepository;
    @Autowired
    private Imagen_servicio iService;
    
    // buscarPorTitulo: String ---> PelicaSerie
    // Busca en la base de datos a la película o serie en cuestión
    public PeliculaSerie buscarPorTitulo (String titulo) throws Errores_servicio{
        return findByTitulo(titulo);
    }
    
    // mostrarTodas: none ---> List<PeliculaSerie>
    // Busca todas las películas o series que estén ingresadas en la base de datos
    public List<PeliculaSerie> mostrarTodas (){
        return findAll();
    }
    
    // crear: Imagen, double, Pesonaje, String ---> PeliculaSerie
    // Crea una nueva película o serie
    public PeliculaSerie crear(MultipartFile archivo, double calificacion,String titulo, java.sql.Date fCreacion)throws Errores_servicio{
        datosParaCrear(archivo, calificacion, titulo, fCreacion);
        PeliculaSerie pos = new PeliculaSerie();
        Imagen imagen = iService.guardar(archivo);
        pos.setImagen(imagen);
        pos.setCalificacion(calificacion);
        pos.setFechaCreacion(fCreacion);
        pos.setTitulo(titulo);
        return posRepository.save(pos);
    }
    
    // editar: String, Imagen, double, Personaje, String ---> PeliculaSerie
    // Edita los datos ingresados de la película o serie que se desea editar
    public PeliculaSerie editar (String id, MultipartFile archivoImg, double calificacion,String titulo, java.sql.Date fCreacion) throws Errores_servicio{
        PeliculaSerie pos = findById(id);
        pos = datosParaEditar(pos, archivoImg, calificacion, titulo, fCreacion);
        return posRepository.save(pos);
    }
    
    // eliminar: String ---> none
    // Elimina a la serie o película solicitada de la base de datos
    public void eliminar (String id) throws Errores_servicio{
        posRepository.delete(findById(id));
    }
    
//    public List<PeliculaSerie> buscarPorGenero (Genero genero){
//        return findByGenero(genero);
//    }
    
    public PeliculaSerie buscarPorId (String id) throws Errores_servicio{
        return findById(id);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// VALIDACIONES //////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    private PeliculaSerie datosParaEditar(PeliculaSerie pos, MultipartFile im, double calif, String titulo, java.sql.Date fCreacion)throws Errores_servicio{ 
        if(im != null){
            pos.setImagen(iService.cambiarImagen(pos.getImagen().getId(), im));
        }
        if(titulo != null && !titulo.equals("")){
            pos.setTitulo(titulo);
        }
        String puntuacion = String.valueOf(calif);
        if(puntuacion != null && !puntuacion.equals("")){
            pos.setCalificacion(calif);
        }
        if (fCreacion != null || !fCreacion.equals("")){
            pos.setFechaCreacion(fCreacion);
        }
        return pos;
    }
    
    private void datosParaCrear(MultipartFile archivo, double calificacion, String titulo, java.sql.Date fCreacion)throws Errores_servicio{
        if(archivo == null){
            throw new Errores_servicio("No ingresó ninguna imágen");
        }
        String calif = String.valueOf(calificacion);
        if(calif == null || calificacion < 0 || calificacion > 5 || calif.isEmpty()){
            throw new Errores_servicio("Debe ingresar una clasificación válida");
        }
        if(titulo == null || titulo.isEmpty()){
            throw new Errores_servicio("Debe ingresar un título válido");
        }
        if(fCreacion == null){
            throw new Errores_servicio("No ingresó una fecha de creación váilda");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////// CONSULTAS A BASE DE DATOS ////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    private PeliculaSerie findByTitulo (String titulo) throws Errores_servicio{
        Optional<PeliculaSerie> pos = posRepository.findByTitulo(titulo);
        if (pos.isPresent()){
            return pos.get();
        }else{
            throw new Errores_servicio("La película o serie buscada no se ha podido encontrar en nuestra base de datos");
        }
    }
    
    private List<PeliculaSerie> findAll(){
        return posRepository.findAll();
    }
    
    private PeliculaSerie findById(String id) throws Errores_servicio{
        Optional<PeliculaSerie> pos = posRepository.findById(id);
        if(pos.isPresent()){
            return pos.get();
        }else{
            throw new Errores_servicio("No ha sido posible encontrar en nuestra base de datos la película o serie ingresada");
        }
    }
    
//    private List<PeliculaSerie> findByGenero (Genero genero){
//        return posRepository.findByGenero(genero);
//    }
    
    
}
