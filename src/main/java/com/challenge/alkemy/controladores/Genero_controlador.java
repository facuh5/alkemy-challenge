/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.controladores;

import com.challenge.alkemy.entidades.Genero;
import com.challenge.alkemy.entidades.Imagen;
import com.challenge.alkemy.entidades.PeliculaSerie;
import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.servicios.Genero_servicio;
import com.challenge.alkemy.servicios.Pelicula_o_Serie_servicio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author usuario
 */
@Controller
@RequestMapping("/genero")
public class Genero_controlador {
    @Autowired
    private Genero_servicio gService;
    @Autowired
    private Pelicula_o_Serie_servicio posService;
    
    //////////////////////// CREAR UN NUEVO GÉNERO /////////////////////////////
    
    @GetMapping("/new-Genero")
    public String crearGenero(){   
    return ""; // html para crear el genero
    }
    
    @PostMapping("new-Genero")
    public String creadoCultivo(@RequestParam MultipartFile image, @RequestParam String nombre, ModelMap modelo){
        try {
            gService.crear(image, nombre);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Genero_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // html para crear el genero
        }
        return "";  // html pantalla de inicio del género
    }
    
    /////////////////////// AGREGAR PELI O SERIE ///////////////////////////////
    
    @GetMapping("/add-Pelicula-Serie-enGenero")
    public String agregarPeliculaSerie(ModelMap modelo){
        modelo.put("pelisSeries", posService.mostrarTodas());
        return "";  // html para agregar película o serie asociada
    } 
    
    @PostMapping("/add-Pelicula-Serie-enGenero")
    public String agregadoPeliculaSerie(ModelMap modelo, @RequestParam String idPeliSerie, @RequestParam String idGenero){
        try {
            gService.aggPoS(idGenero, idPeliSerie);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Genero_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return ""; // html para agregar película o serie asociada
        }
        return ""; // html pantalla de inicio del género
    }
    
    //////////////////////// EDITAR PELI O SERIE ///////////////////////////////
    
    @GetMapping("/edit-Genero")
    public String editarGenero(ModelMap modelo){
        List<Genero> gens = gService.mostrarTodos();
        List<String> nombres = new ArrayList<>();
        for (Genero gen : gens) {
            nombres.add(gen.getNombre());
        }
        modelo.put("Generos", nombres);
        return "";  // html para editar el género
    }
    
    @PostMapping("/edit-Genero/save")
    public String editadoGenero(ModelMap modelo, @RequestParam String idGen, @RequestParam MultipartFile imagen, @RequestParam PeliculaSerie peliOserie, @RequestParam String nombre){
        try {
            gService.editar(idGen, imagen, nombre, peliOserie); // la PeliculaSerie introducida, se elimina de la lista del género.
        } catch (Errores_servicio ex) {
            Logger.getLogger(Genero_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return ""; // html para editar el género
        }
        return ""; // html pantalla de inicio de género
    }
            
}
