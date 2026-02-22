package com.example.projectapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Diz ao Spring que esta classe contém definições de "Beans" (objetos gerenciados pelo Spring).
public class PasswordHash {

    @Bean // Bean é uma forma de instanciar no IoC da aplicação um objeto que pode ser utilizado em outro momento
    // do codigo sem precisar usar o new.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
