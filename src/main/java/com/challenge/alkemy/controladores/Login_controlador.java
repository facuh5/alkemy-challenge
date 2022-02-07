/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author usuario
 */
@Controller
@RequestMapping("/auth/login")
public class Login_controlador {
    
    @GetMapping("")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Username o Password INCORRECTOS");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido CORRECTAMENTE de la plataforma");
        }
        return "login";
    }
}
