/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.controladores;

import com.challenge.alkemy.entidades.Genero;
import com.challenge.alkemy.entidades.Imagen;
import com.challenge.alkemy.entidades.PeliculaSerie;
import com.challenge.alkemy.entidades.Personaje;
import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.servicios.Genero_servicio;
import com.challenge.alkemy.servicios.Pelicula_o_Serie_servicio;
import com.challenge.alkemy.servicios.Personaje_servicio;
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
@RequestMapping("/movies")
public class Pelicula_o_Serie_controlador {
    
    @Autowired
    private Pelicula_o_Serie_servicio posService;
    
    @Autowired 
    private Personaje_servicio pService;
    
    @Autowired
    private Genero_servicio gService;
    
    @GetMapping("") // SOLO MUESTRA IMAGEN, TITULO Y FECHA DE CREACION
    public String listadoPeliculas(ModelMap modelo){
        List<PeliculaSerie> listado = posService.mostrarTodas();
        List<Imagen> imags = new ArrayList<>();
        List<String> titulos = new ArrayList<>();
        List<java.sql.Date> fechas = new ArrayList<>();
        for (PeliculaSerie peliculaSerie : listado) {
            imags.add(peliculaSerie.getImagen());
            titulos.add(peliculaSerie.getTitulo());
            fechas.add(peliculaSerie.getFechaCreacion());
        }
        modelo.put("imagenes", imags);
        modelo.put("titulos", titulos);
        modelo.put("fechasCreacion", fechas);
        return "";  // html que lleva al listado de las peliculas
    }
    
    @GetMapping("/detail") // MUESTRA TODOS LOS CAMPOS DE LA PoS Y LOS PERSONAJES ASOCIADOS
    public String detalle(ModelMap modelo){
        List<PeliculaSerie> pos = posService.mostrarTodas();
        List<String> titulos = new ArrayList<>();
        for (PeliculaSerie po : pos) {
            titulos.add(po.getTitulo());
        }
        modelo.put("titulos", titulos);
        return ""; // html con el nombre de todas las películas o series, de las cuales, la que cliquea se abre
    }
    
    @PostMapping("/detail")
    public String detallado(ModelMap modelo, @RequestParam String idPoS){
        try {
            PeliculaSerie pos = posService.buscarPorId(idPoS);
            List<Personaje> personajesAsoc = pService.personajesAsocPoS(idPoS);
            modelo.put("calificacion", pos.getCalificacion());
            modelo.put("fechaCreacion", pos.getFechaCreacion());
            modelo.put("imagen", pos.getImagen());
            modelo.put("titulo", pos.getTitulo());
            modelo.put("personajesAsoc", personajesAsoc);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Pelicula_o_Serie_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return ""; // mismo html del método Get
        }
        return "";  // html que muestra en detalle la película elegida
    }
    
    @GetMapping("/create") // CREA UN PERSONAJE NUEVO
    public String crear(){
        return "";  // html para completar los datos de la pelicula o serie nueva
    } 
    
    @PostMapping("/create")
    public String creando(@RequestParam String tit, @RequestParam MultipartFile im, @RequestParam java.sql.Date fCreac, @RequestParam double calif, ModelMap modelo){
        try {
            posService.crear(im, calif, tit, fCreac);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Pelicula_o_Serie_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // mismo html del metodo get
        }
        return "";  // html que lleva al listado de las películas
    }
    
    // ver el tema de agg el género y personaje asociado
    
    @GetMapping("/edit")
    public String editar(ModelMap modelo){
        List<PeliculaSerie> posS = posService.mostrarTodas();
        List<String> titulos = new ArrayList<>();
        for (PeliculaSerie peliculaSerie : posS) {
            titulos.add(peliculaSerie.getTitulo());
        }
        modelo.put("titulos", titulos);
        return "";  //  html para elegir la película o serie a modificar y agregar los datos que se modificarán
    }
    
    @PostMapping("/edit")
    public String editando (ModelMap modelo, @RequestParam String idPoS, @RequestParam String tit, @RequestParam double calif, @RequestParam MultipartFile arch, @RequestParam java.sql.Date fCreac){
        try {
            posService.editar(tit, arch, calif, tit, fCreac);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Pelicula_o_Serie_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // mismo html que el método Get
        }
        return "";  // html que muestra el listado de todas las películas
    }
    
    // ver el tema de sacar la película de todos los personajes y del genero que le corresponda
    
    @GetMapping("/delete")
    public String borrar(ModelMap modelo){
        List<PeliculaSerie> posS = posService.mostrarTodas();
        List<String> titulos = new ArrayList<>();
        for (PeliculaSerie peliculaSerie : posS) {
            titulos.add(peliculaSerie.getTitulo());
        }
        modelo.put("titulos", titulos);
        return "";  // html donde se selecciona a la película o serie que se quiere borrar del listado dado
    }
    
    @PostMapping("/delete")
    public String borrando(ModelMap modelo, @RequestParam String idPoS){
        try {
            posService.eliminar(idPoS);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Pelicula_o_Serie_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return ""; // mismo html que el Get
        }
        return "";  // html que muestra el listado de las películas o series
    }
    
    // ver q al eliminar el PoS, se lo debe previamente sacar de todos los personajes y del genero
    
    @GetMapping("/search")
    public String BusquedaNombre(){
        return "";  // html que permite buscar por nombre
    }
    
    @GetMapping("/search-name")
    public String buscarNombre(ModelMap modelo, @RequestParam String tit){
        try {
            PeliculaSerie pos = posService.buscarPorTitulo(tit);
            List<Personaje> personajesAsoc = pService.personajesAsocPoS(pos.getId());
            modelo.put("calificacion", pos.getCalificacion());
            modelo.put("fechaCreacion", pos.getFechaCreacion());
            modelo.put("imagen", pos.getImagen());
            modelo.put("titulo", pos.getTitulo());
            modelo.put("personajesAsoc", personajesAsoc);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Pelicula_o_Serie_controlador.class.getName()).log(Level.SEVERE, null, ex);
            return "";  // mismo html que el método Get
        }
        return "";  // html donde te muestra en detalle la película o serie (usado en url '/movies/detail'
    }
    
    @GetMapping ("/search-genero")
    public String buscarGenero(ModelMap modelo){
        List<Genero> generos = gService.mostrarTodos();
        List<String> gNombre = new ArrayList<>();
        for (Genero genero : generos) {
            gNombre.add(genero.getNombre());
        }
        modelo.put("generos", gNombre);
        return "";  // html que te permite elegir sobre qué genero filtrar
    }
    
    @PostMapping("/search-genero")
    public String buscandoGereno(ModelMap modelo, @RequestParam String idGen){
        try {
            Genero gen = gService.buscarPorId(idGen);
            List<PeliculaSerie> pos = gen.getPeliculas_o_series_asociadas();
            modelo.put("peliculasSeries", pos);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Pelicula_o_Serie_controlador.class.getName()).log(Level.SEVERE, null, ex);
            return "";  // mismo html que método Get
        }
        return "";
    }
    
}
