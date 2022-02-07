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
import com.challenge.alkemy.repositorios.Personaje_repositorio;
import java.util.ArrayList;
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
public class Personaje_servicio {
    @Autowired
    Personaje_repositorio pRepository;
    
    @Autowired
    Pelicula_o_Serie_servicio posService;
    
    @Autowired
    Imagen_servicio iService;
    
    // crearPersonaje: Image, String, double, int, String, List ---> Personaje
    // Recibe los datos necesarios para crear un nuevo personaje
    public Personaje crear(MultipartFile arch, String nom, double kg, int ed, String hist) throws Errores_servicio{
        validarPersonajeNuevo(arch, nom, kg, ed, hist);
        Personaje personaje = new Personaje();
        personaje.setEdad(ed);
        personaje.setHistoria(hist);
        Imagen imagen = iService.guardar(arch);
        personaje.setImagen(imagen);
        personaje.setNombre(nom);
        //personaje.setPeliculas_o_series_asociadas(pos);
        personaje.setPeso(kg);
        pRepository.save(personaje);
        return personaje;
    }
    
    // agregarPeliculaSerieAsociada: String, String ---> Personaje
    // Agrega al personaje cuyo id es ingresado la película o serie cuyo id es ingresado
    public Personaje agregarPeliculaSerieAsociada (String idPersonaje, String idPeliSer)throws Errores_servicio{
        Personaje per = findById(idPeliSer);
        PeliculaSerie pelSer = posService.buscarPorId(idPeliSer);
        List<PeliculaSerie> listado = per.getPeliculas_o_series_asociadas();
        if (!listado.contains(pelSer)){
            listado.add(pelSer);
        }
        per.setPeliculas_o_series_asociadas(listado);
        return pRepository.save(per);
    }
    
    // vaciarPoSasociadas: String ---> Personaje
    // Elimina las peliculas o series asociadas al personaje
    private Personaje vaciarPoSasociadas(String idPersonaje) throws Errores_servicio{
        Personaje per = findById(idPersonaje);
        List<PeliculaSerie> vacia = new ArrayList<>();
        per.setPeliculas_o_series_asociadas(vacia);
        return pRepository.save(per);
    }
    
    // eliminarPoSasociada: String, String ---> Personaje
    // quita de la lista de peliculas o series asociadas la dada
    private Personaje eliminarPoSasociada (String idPersonaje, String idPoS)throws Errores_servicio{
        Personaje per = findById(idPersonaje);
        PeliculaSerie pos = posService.buscarPorId(idPoS);
        List<PeliculaSerie> pelisSers = per.getPeliculas_o_series_asociadas();
        if (pelisSers.contains(pos)){
            pelisSers.remove(pos);
        }
        per.setPeliculas_o_series_asociadas(pelisSers);
        return pRepository.save(per);
    }
    
    public void opcEditPoS(String idPers, String idPoS)throws Errores_servicio{
        if(idPoS == null || idPoS.isEmpty()){
            vaciarPoSasociadas(idPers);
        } else {
            eliminarPoSasociada(idPers, idPoS);
        }
    }
    
    // editar: String, Image, String, double, int, String, List ---> Personaje
    public Personaje editar(String id, MultipartFile arch, String nom, double kg, int ed, String hist, PeliculaSerie pos) throws Errores_servicio{
        Personaje per = findById(id);
        per = datosParaEditar(per, arch, nom, kg, ed, hist, pos);
        pRepository.save(per);
        return per;
    }
    
    // eliminar: String ---> none
    // Elimina al personaje correspondiente
    public void eliminar(String id) throws Errores_servicio{
        Personaje per = findById(id);
        pRepository.delete(per);
    }
    
    // detalle: String ---> Personaje
    // Trae toda la información del personaje solicitado
    public Personaje detalle(String id) throws Errores_servicio{
        return findById(id);
    }
    
    // buscarPorNombre: String ---> Personaje
    // Busca en la base de datos al personaje cuyo nombre fue ingresado
    public Personaje buscarPorNombre (String nombre) throws Errores_servicio{
        return findByNombre(nombre);
    }
    
    // buscarPorEdad: int ---> List<Personaje>
    // Busca en la base de datos todos los personajes que tienen la edad ingresada
    public List<Personaje> buscarPorEdad (int edad) throws Errores_servicio{
        return findByEdad(edad);
    }
    
    // buscarPorPeso: double ---> List<Personaje>
    // Busca en la base de datos todos los personajes que pesan lo mismo que lo ingresado
    public List<Personaje> buscarPorPeso (double peso) throws Errores_servicio{
        return findByPeso(peso);
    }
    
    // buscarPorPeliculaSerie: String ---> List<Personaje>
    // Busca en la base de datos todos los personajes que participan en determinada serie o película
//    public List<Personaje> buscarPorPeliculaSerie (String peliOserie) throws Errores_servicio{
//        PeliculaSerie pos = posService.buscarPorTitulo(peliOserie);
//        List<Personaje> pers = findAll();
//        List<Personaje> persPoS = new ArrayList<>();
//        for (Personaje per : pers) {
//            for (PeliculaSerie pelOserSsoc : per.getPeliculas_o_series_asociadas()) {
//                if(pelOserSsoc.equals(pos)){
//                    persPoS.add(per);
//                    break;
//                }
//            }
//        }
//        return persPoS;
//    }
    
    public List<Personaje> mostrarTodos (){
        return findAll();
    }
    
    
    public List<Personaje> personajesAsocPoS (String idPoS) throws Errores_servicio{
        PeliculaSerie pos = posService.buscarPorId(idPoS);
        List<Personaje> personajes = mostrarTodos();
        List<Personaje> persAsocPos = new ArrayList<>();
        for (Personaje personaje : personajes) {
            for (PeliculaSerie posAsoc : personaje.getPeliculas_o_series_asociadas()) {
                if(pos.equals(posAsoc)){
                    persAsocPos.add(personaje);
                }
            }
        }
        return persAsocPos;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /////////////////////////// VALIDACIONES ///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // datosParaEditar: Personaje, Image, String, double, int, String, List ---> Personaje
    // Recibe los datos que componen al personaje y verifica que puedan modificar los ya existentes
    private Personaje datosParaEditar(Personaje per, MultipartFile im, String nom, double kg, int ed, String hist, PeliculaSerie pos)throws Errores_servicio{
        if(im != null){
            per.setImagen(iService.cambiarImagen(per.getImagen().getId(), im));
        }
        if(nom != null && !nom.equals("")){
            per.setNombre(nom);
        }
        String peso = String.valueOf(kg);
        if(peso != null && !peso.equals("")){
            per.setPeso(kg);
        }
        if(hist != null && !hist.equals("")){
            per.setHistoria(hist);
        }
        String edad = String.valueOf(ed);
        if(edad != null && !edad.equals("")){
            per.setEdad(ed);
        }
//        if(pos != null){
//            List<PeliculaSerie> listado = per.getPeliculas_o_series_asociadas();
//            for (PeliculaSerie peliculaSerie : listado) {
//                if (peliculaSerie == pos){
//                    listado.remove(pos);
//                    break;
//                }
//            }
//            per.setPeliculas_o_series_asociadas(listado);
//        }
        return per;
    }
    
    // validarPersonajeNuevo: Image, String, double, int, String, List ---> none || Error
    // Verifica la validez de los datos ingresados para crear un personaje nuevo
    private void validarPersonajeNuevo (MultipartFile im, String nom, double kg, int ed, String hist) throws Errores_servicio{
        if(nom == null || nom.equals("")){
            throw new Errores_servicio("Debe ingresar un nombre válido");
        }
        if (im == null){
            throw new Errores_servicio("debe ingresar una imágen válida");
        }
        String peso = String.valueOf(kg);
        if(peso == null || peso.equals("") || kg <= 0){
            throw new Errores_servicio("Debe ingresar un peso válido");
        }
        if(hist == null || hist.equals("")){
            throw new Errores_servicio("Debe ingresar una historia válida");
        }
        String edad = String.valueOf(ed);
        if(edad == null || edad.equals("") || ed <= 0){
            throw new Errores_servicio("Debe ingresar una edad válida");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////// CONSULTAS A BASE DE DATOS ////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    private Personaje findById (String id) throws Errores_servicio{
        Optional<Personaje> per = pRepository.findById(id);
        if (per.isPresent()){
            return per.get();
        } else {
            throw new Errores_servicio("El personaje solicitado no se encuentra ingresado en nuestra base de datos");
        }
    }
    
    private Personaje findByNombre (String nombre)throws Errores_servicio{
        Optional<Personaje> per = pRepository.findByNombre(nombre);
        if (per.isPresent()){
            return per.get();
        } else {
            throw new Errores_servicio("El personaje que quiere buscar no se encuentra en nuestra base de datos");
        }
    }
    
    private List<Personaje> findByEdad (int edad) throws Errores_servicio{
        Optional<List<Personaje>> pers = pRepository.findByEdad(edad);
        if (pers.isPresent()){
            return pers.get();
        } else {
            throw new Errores_servicio("No se encontró a ningún personaje con la edad solicitada");
        }
    }
    
    private List<Personaje> findByPeso (double peso) throws Errores_servicio{
        Optional<List<Personaje>> pers = pRepository.findByPeso(peso);
        if (pers.isPresent()){
            return pers.get();
        } else {
            throw new Errores_servicio("No se encontró ningún personaje que cumpla con la condición del peso ingresada");
        }
    }
    
    private List<Personaje> findAll (){
        return pRepository.findAll();
    }
}
