/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.servicios;

import com.challenge.alkemy.entidades.Imagen;
import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.repositorios.Imagen_repositorio;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author usuario
 */
@Service
public class Imagen_servicio {
    
    @Autowired
    private Imagen_repositorio iRepository;
    
    public Imagen guardar(MultipartFile archivo)throws Errores_servicio{
        if(archivo == null){
            return null;
        } else {
            try {
                Imagen img = new Imagen();
                img.setMime(archivo.getContentType());
                img.setNombre(archivo.getName());
                img.setContenido(archivo.getBytes());
                return iRepository.save(img);
            } catch (IOException ex) {
                Logger.getLogger(Imagen_servicio.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
    }
    
    public Imagen cambiarImagen (String idImg, MultipartFile archivo) throws Errores_servicio{
        try{
            Imagen img = findById(idImg);
            if(archivo != null){
                img.setContenido(archivo.getBytes());
                img.setMime(archivo.getContentType());
                img.setNombre(archivo.getName());
                return iRepository.save(img);
            }
        } catch (IOException ex) {
            Logger.getLogger(Imagen_servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;
        }
        
    
    private Imagen findById(String id)throws Errores_servicio{
        Optional<Imagen> img = iRepository.findById(id);
        if(img.isPresent()){
            return img.get();
        }else{
            throw new Errores_servicio("No se ha encontrado la foto solicitada en la base de datos");
        }
    }
}
