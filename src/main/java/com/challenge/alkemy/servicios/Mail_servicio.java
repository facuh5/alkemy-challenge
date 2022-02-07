/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class Mail_servicio {
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    public void enviarMail(String destinatario, String asunto, String contenido){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(destinatario);
        smm.setSubject(asunto);
        smm.setText(contenido);
        javaMailSender.send(smm);
    }
}
