/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.controladores;

import com.challenge.alkemy.servicios.Mail_servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author usuario
 */
@Controller
@RequestMapping("/")
public class Main_controlador {
    
    @Autowired
    private Mail_servicio mService;
    
    @GetMapping("")
    public String nnds (){
        return "index";
    }
    
    @PostMapping("send-email")
    public String enviarMail(@RequestParam String destinatario, @RequestParam String asunto, @RequestParam String contenido){
        mService.enviarMail(destinatario, asunto, contenido);
        return "";  // html de pantalla de inicio por ejemplo
    }
    
    @PostMapping("send-email-register")
    public String enviaMail(@RequestParam String email){
        mService.enviarMail(email, "Creación de su usuario", "Queremos informarle que su cuenta ha sido creada exitosamente\nEsperamos que pueda disfrutarla al máximo!\nSaludos cordiales.");
        return "";  // html de la pantalla de inicio
    }
}
