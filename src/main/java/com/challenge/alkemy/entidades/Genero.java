/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author usuario
 */
@Entity
public class Genero implements Serializable{
    //Atributos:
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    @OneToOne
    private Imagen imagen;
    @OneToMany
    private List<PeliculaSerie> peliculas_o_series_asociadas = new ArrayList<>();

    //métodos:
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public List<PeliculaSerie> getPeliculas_o_series_asociadas() {
        return peliculas_o_series_asociadas;
    }

    public void setPeliculas_o_series_asociadas(List<PeliculaSerie> peliculas_o_series_asociadas) {
        this.peliculas_o_series_asociadas = peliculas_o_series_asociadas;
    }
    
    
}
