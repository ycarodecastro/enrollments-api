package com.example.projectapi.infra.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.rate-limit")
public class RedisRateLimitProperties {

    private int userMaxAttempts;
    private int ipMaxAttempts;
    private int blockDurationMinutes;
}
