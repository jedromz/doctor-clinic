package com.jedromz.doctorclinic.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
@Slf4j
public class EmailConfiguration {
    private String host;
    private int port;
    private String username;
    private String password;

    @Bean
    public JavaMailSender mailSender() {
        log.info("emailConfig: {},{},{},{}", host, port, username, password);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(getHost());
        mailSender.setPort(getPort());
        mailSender.setUsername(getUsername());
        mailSender.setPassword(getPassword());
        return mailSender;
    }
}
