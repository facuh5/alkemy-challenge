/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.challenge.alkemy;

import com.challenge.alkemy.servicios.Usuario_servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author usuario
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter{
    
//    @Autowired
//    private User_servicio userService;
    
    @Autowired 
    private Usuario_servicio uSerivice;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(uSerivice).passwordEncoder(new BCryptPasswordEncoder());//passwordEncoder());
    }
    
     @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin()
                .and().authorizeRequests()
                .antMatchers("/CSS/*", "/IMG/*", "/JS/*").permitAll()
                .antMatchers("/").permitAll()
                .and().formLogin()
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("contrasenia")
                .loginProcessingUrl("/auth/logincheck")
                .defaultSuccessUrl("/")
                .failureUrl("/auth/login?error=error").permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and().csrf().disable();
    }
    
}
