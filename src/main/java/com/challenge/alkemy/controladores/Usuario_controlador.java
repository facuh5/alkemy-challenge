/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.controladores;

import com.challenge.alkemy.errores.Errores_servicio;
import com.challenge.alkemy.servicios.Mail_servicio;
import com.challenge.alkemy.servicios.Usuario_servicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author usuario
 */
@Controller
@RequestMapping("/auth")
public class Usuario_controlador {
    
    @Autowired
    private Usuario_servicio uService;
    @Autowired
    private Mail_servicio mService;

    
    
    @GetMapping("/register")
    public String registrar(){
        return "registro";  // html con formulario de registro
    }
    
    @PostMapping("/register")
    public String registrado(ModelMap modelo, @RequestParam String nombre, @RequestParam String contra1, @RequestParam String contra2, @RequestParam String email){
        try {
            uService.crear(nombre, contra1, contra2, email);
            mService.enviarMail(email, "Creación de su usuario", "Queremos informarle que su cuenta ha sido creada exitosamente\nEsperamos que pueda disfrutarla al máximo!\nSaludos cordiales.");
        } catch (Errores_servicio ex) {
            Logger.getLogger(Usuario_controlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            modelo.put("email", email);
            modelo.put("nombre", nombre);
            return "registro";  // html con formulario de registro
        }
        return "redirect:/";  // html de pantalla de inicio   redirect:/auth/send-email-register
    }
    
}
