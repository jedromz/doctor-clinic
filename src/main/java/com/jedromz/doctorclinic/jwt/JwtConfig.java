package com.jedromz.doctorclinic.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
    private Integer tokenExpirationAfterMinutes;
    private Integer refreshTokenExpirationAfterMinutes;
    private Integer refreshTokenExpirationAfterMillis;
    private Integer tokenExpirationAfterMillis;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(getSecretKey().getBytes());
    }
}
