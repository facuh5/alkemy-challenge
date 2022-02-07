/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.controladores;

import com.challenge.alkemy.entidades.Imagen;
import com.challenge.alkemy.entidades.PeliculaSerie;
import com.challenge.alkemy.entidades.Personaje;
import com.challenge.alkemy.errores.Errores_servicio;
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
@RequestMapping("/charecters")
public class Personaje_controlador {
    
    @Autowired
    private Personaje_servicio pService;
    @Autowired
    private Pelicula_o_Serie_servicio posService;
    
    ////////////////// LISTADO SIN DETALLAR DE PERSONAJES //////////////////////
    
    @GetMapping("")
    public String listadoPersonajes(ModelMap modelo){
        List<Personaje> personajes = pService.mostrarTodos();
        List<Imagen> imgs = new ArrayList<>();
        List<String> nombres = new ArrayList<>();
        for (Personaje personaje : personajes) {
            nombres.add(personaje.getNombre());
            imgs.add(personaje.getImagen());
        }
        modelo.put("nombres", nombres);
        modelo.put("imagenes", imgs);
        return "";  // html donde se muestra el listado de los personajes
    }
    
    //////////////////////// CREACIÓN DE UNO NUEVO /////////////////////////////
    
    @GetMapping("/new-Personaje")
    public String crearPersonaje(){
        return "nuevoPersonaje";  // html para crear el personaje
    }
    
    @PostMapping("/new-Personaje")
    public String creadoPersonaje(ModelMap modelo, @RequestParam MultipartFile im, @RequestParam String nom, @RequestParam double peso, @RequestParam int edad, @RequestParam String hist){
        try {
            pService.crear(im, nom, peso, edad, hist);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "nuevoPersonaje";  // html para crear el personaje
        }
        return "index";  // html pantalla inicio de character/personaje
    }
    
    //////////////////////// AGREGAR PELIS O SERIES ////////////////////////////
    
    @GetMapping("/add-Pelicula-Serie-Personaje")
    public String agregarPeliculaSerie (ModelMap modelo){
        List<PeliculaSerie> posS = posService.mostrarTodas();
        List<String> titulos = new ArrayList<>();
        for (PeliculaSerie peliculaSerie : posS) {
            titulos.add(peliculaSerie.getTitulo());
        }
        List<Personaje> pers = pService.mostrarTodos();
        List<String> nombres = new ArrayList<>();
        for (Personaje per : pers) {
            nombres.add(per.getNombre());
        }
        modelo.put("pelisSers", titulos);
        modelo.put("personajes", nombres);
        return "";  // html para agregarle la película o serie a un personaje
    }
    
    @PostMapping("/add-Pelicula-Serie-Personaje")
    public String agregadoPeliculaSerie (ModelMap modelo,@RequestParam String idPersonaje,@RequestParam String idPeliSer){
        try {
            pService.agregarPeliculaSerieAsociada(idPersonaje, idPeliSer);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // html para agregar la película o serie a un personaje
        }
        return "";  // html pantalla inicio de character/personaje
    }
    
    ////////////////////////// EDITAR PERSONAJE ////////////////////////////////
    
    @GetMapping("/edit-Personaje")
    public String editarPersonaje(ModelMap modelo) {
        List<Personaje> pers = pService.mostrarTodos();
        List<String> nombres = new ArrayList<>();
        for (Personaje per : pers) {
            nombres.add(per.getNombre());
        }
        modelo.put("personajes", nombres);
        return "";  // html para editar el personaje
    }
    
    @PostMapping("/edit-Personaje")
    public String editadoPersonaje(ModelMap modelo, @RequestParam String idPer, @RequestParam MultipartFile im, @RequestParam String nombre, @RequestParam double peso, @RequestParam int edad, @RequestParam String historia, @RequestParam PeliculaSerie pos){
        try {
            pService.editar(idPer, im, nombre, peso, edad, historia, pos);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // html para editar el personaje
        }
        return "";  // html pantalla inicio de character/personaje
    }
    
    
    @GetMapping("/edit-Personaje/Pelicula-Serie-asoc")
    public String editPoS(ModelMap modelo){
        List<Personaje> pers = pService.mostrarTodos();
        List<String> nombres = new ArrayList<>();
        for (Personaje per : pers) {
            nombres.add(per.getNombre());
        }
        modelo.put("personajes", nombres);
        return "";  // html que te permite elegir si quitar alguno de las PoS asociadas al personaje o si quitar todas
    }
    
    @PostMapping("/edit-Personaje/Pelicula-Serie-asoc")
    public String editarPoS(ModelMap modelo, @RequestParam String idPers, @RequestParam String idPoS){
        try {
            pService.opcEditPoS(idPers, idPoS);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
        }
        return "";  // html pantalla inicio de character/personaje
    }
    ////////////////////////// ELIMINAR PERSONAJE //////////////////////////////
    
    @GetMapping("delete-Personaje")
    public String eliminarPersonaje(){
        return "";  // html para eliminar el personaje
    }
    
    @PostMapping("/delete-Personaje")
    public String eliminadoPersonaje(ModelMap modelo, @RequestParam String idPersonaje){
        try {
            pService.eliminar(idPersonaje);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // html para eliminar el personaje
        }
        return "";  // html pantalla inicio de character/personaje
    }
    
    //////////////////////// PERSONAJE EN DETALLE //////////////////////////////
    
    @GetMapping("/detail-Personaje")
    public String verPersonajeDetallado(ModelMap modelo){
        List<Personaje> pers = pService.mostrarTodos();
        List<String> nombres = new ArrayList<>();
        for (Personaje per : pers) {
            nombres.add(per.getNombre());
        }
        modelo.put("personajes", nombres);
        return "";  // html que permite seleccionar el personaje del que se quiere conocer la información en detalle
    }
    
    @PostMapping("/detail-Personaje")
    public String vistoPersonajeDetallado (ModelMap modelo, @RequestParam String idPersonaje){
        try {
            Personaje per = pService.detalle(idPersonaje);
            modelo.put("imagen", per.getImagen());
            modelo.put("edad", per.getEdad());
            modelo.put("historia", per.getHistoria());
            modelo.put("nombre", per.getNombre());
            modelo.put("peso", per.getPeso());
            modelo.put("pelisSers", per.getPeliculas_o_series_asociadas());
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";  // html que permite selleccionar el personaje del que se quiere conocer la información en detalle
        }
        return "";  // html que permite mostrar en pantalla la información detallada
    }
    
    /////////////// BÚSQUEDAS Y FILTROS EN BASE DE DATOS ///////////////////////
    
    @GetMapping("/search")
    public String pagBusqueda(){
        return "";  // html que te da las opciones de busqueda que hay (por nombre, por filtro, etc)
    }
    
    @GetMapping("/search/name")
    public String buscarPorNombre(){
        return "";  // html que te permite hacer la búsqueda por nombre
    }
    
    @PostMapping("/search/name")
    public String buscarNombre(ModelMap modelo, @RequestParam String nombre){
        try {
            Personaje per = pService.buscarPorNombre(nombre);
            modelo.put("personaje", per);
        } catch (Errores_servicio ex) {
            Logger.getLogger(Personaje_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "";
        }
        return "";
    }
    
}
