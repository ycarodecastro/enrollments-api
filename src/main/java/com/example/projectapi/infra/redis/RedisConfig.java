package com.example.projectapi.infra.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

// Anotação que indica que esta classe contém configurações de beans para o Spring..
@Configuration
public class RedisConfig {

    // Anotação que adiciona esse metodo no contexto da aplicação.
    @Bean

    // A classe "StringRedisTemplate" facilita operações no Redis usando chaves e valores do tipo String.
    // Esse metodo cria e disponibiliza esse bean para ser usado na aplicação.
        public StringRedisTemplate redisTemplate(RedisConnectionFactory factory /* conexão de fato */ ) {
        return new StringRedisTemplate(factory);
    }
}
