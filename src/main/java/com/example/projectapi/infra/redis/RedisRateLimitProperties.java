package com.example.projectapi.infra.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component

// Diz ao Spring Boot que os atributos dessa classe devem ser preenchidos com valores
// vindos do arquivo de configuração (application.properties ou application.yml),
// usando o prefixo "security.rate-limit".
@ConfigurationProperties(prefix = "security.rate-limit")
public class RedisRateLimitProperties {

    private int userMaxAttempts;
    private int ipMaxAttempts;
    private int blockDurationMinutes;
}
