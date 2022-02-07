/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.servicios;

import com.challenge.alkemy.entidades.Genero;
import com.challenge.alkemy.entidades.Imagen;
import com.challenge.alkemy.entidades.PeliculaSerie;
import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.repositorios.Genero_repositorio;
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
public class Genero_servicio {
    @Autowired 
    private Genero_repositorio gRepository;
    
    @Autowired
    private Pelicula_o_Serie_servicio posService;
    
    @Autowired
    private Imagen_servicio iService;
    
    public Genero crear (MultipartFile archivo, String nombre) throws Errores_servicio{
       validarCrear(archivo, nombre);
        Genero gen = new Genero();
        Imagen imagen = iService.guardar(archivo);
        gen.setImagen(imagen);
        gen.setNombre(nombre);
        return gRepository.save(gen);
    }
    
    private void validarCrear (MultipartFile imagen, String nombre) throws Errores_servicio{
        if (imagen == null){
            throw new Errores_servicio("La imagen ingresada debe ser válida");
        }
        if(nombre == null || nombre.equals("")){
            throw new Errores_servicio("El nombre del género ingresado debe ser válido");
        }
    }
    
    public Genero aggPoS(String idGen, String idPoS) throws Errores_servicio{
        Genero gen = buscarPorId(idGen);
        PeliculaSerie pos = posService.buscarPorId(idPoS);
        List<PeliculaSerie> pelis = gen.getPeliculas_o_series_asociadas();
        if(!pelis.contains(pos)){
            pelis.add(pos);
            gen.setPeliculas_o_series_asociadas(pelis);
        }
        return gRepository.save(gen);
    }
        
    public Genero editar (String id, MultipartFile imagen, String nombre, PeliculaSerie peliOserie) throws Errores_servicio{
        Genero gen = findById(id);
        return gRepository.save(validarEditar(gen, imagen, nombre, peliOserie));
    }
    
    private Genero validarEditar (Genero gen, MultipartFile imagen, String nombre, PeliculaSerie peliOserie) throws Errores_servicio{
        if (imagen != null){
            gen.setImagen(iService.cambiarImagen(gen.getImagen().getId(), imagen));
        }
        if(nombre != null || !nombre.equals("")){
            gen.setNombre(nombre);
        }
        if(peliOserie != null){
            List<PeliculaSerie> listado = gen.getPeliculas_o_series_asociadas();
            for (PeliculaSerie peliculaSerie : listado) {
                if(peliculaSerie.equals(peliOserie)){
                    listado.remove(peliOserie);
                }
            }
            gen.setPeliculas_o_series_asociadas(listado);
        }
        return gen;
    }
    
    public Genero buscarPorId (String id) throws Errores_servicio{
        return findById(id);
    }
    
    private Genero findById(String id) throws Errores_servicio{
        Optional<Genero> gen = gRepository.findById(id);
        if(gen.isPresent()){
            return gen.get();
        }else{
            throw new Errores_servicio("El género que solicitó buscar no se encuentra en nuestra base de datos");
        }
    }
    
    public List<Genero> mostrarTodos(){
        return findAll();
    }
    
    private List<Genero> findAll(){
        return gRepository.findAll();
    }
}
