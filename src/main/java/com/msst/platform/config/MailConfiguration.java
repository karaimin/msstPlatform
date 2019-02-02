package com.msst.platform.config;

import io.github.jhipster.config.JHipsterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

//@Configuration
public class MailConfiguration {

//    @Autowired
    private Environment env;

//    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", Boolean.parseBoolean(env.getProperty("spring.mail.properties.mail.smtp.auth")));
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.starttls.enable",  Boolean.parseBoolean(env.getProperty("spring.mail.properties.mail.smtp.starttls.enable")));
        mailProperties.put("mail.smtp.starttls.required", true);
        mailProperties.put("mail.smtp.debug", true);
        mailProperties.put("mail.smtp.connectiontimeout", 2000);
        mailProperties.put("mail.smtp.timeout", 2000);
//        mailProperties.put("mail.smtp.socketFactory.fallback", false);

        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setProtocol("smtp");

        return mailSender;
    }
}
