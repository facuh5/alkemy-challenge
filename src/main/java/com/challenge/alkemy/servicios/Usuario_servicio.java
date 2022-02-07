/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.servicios;

import com.challenge.alkemy.entidades.Usuario;
import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.repositorios.Usuario_repositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class Usuario_servicio implements UserDetailsService{
    @Autowired
    Usuario_repositorio uRepository;
    
    // crear: String, String, String ---> Usuario
    // Si los datos ingresados son válidos crea un nuevo usuario
    public Usuario crear (String nombre, String contra1, String contra2, String email)throws Errores_servicio{
        validarCrear(nombre, contra1, contra2, email);
        Usuario usu = new Usuario();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usu.setContrasenia(encoder.encode(contra1));
        usu.setNombre(nombre);
        usu.setEmail(email);
        return uRepository.save(usu);
    }
    
    
    private void validarCrear (String nombre, String contra1, String contra2, String email)throws Errores_servicio{
        if (null == nombre || nombre.equals("")){
            throw new Errores_servicio("El nombre de usuario debe ser válido");
        }
        if (buscarPorNombreOp(nombre).isPresent()){
            throw new Errores_servicio("El nombre de usuario que intenta registrar ya se encuentra en uso");
        }
        if (contra1 == null || contra1.equals("") || !contra1.equals(contra2)){
            throw new Errores_servicio("Las contraseñas deben ser válidas e iguales");
        }
        if (null == email || email.equals("")){
            throw new Errores_servicio("El email del usuario debe ser válido");
        }
    }
    
    private Optional<Usuario> buscarPorNombreOp(String nombre){
        return uRepository.findByNombre(nombre);
    }
    
    public Usuario buscarPorEmail(String email){
        return uRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usu = buscarPorEmail(email);
            User user;
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("CLIENTE"));
            return new User(email, usu.getContrasenia(), authorities);
        }catch(Exception ex){
            throw new UsernameNotFoundException("El usuario que ha ingresado no existe por el momento");
        }
    }
}
