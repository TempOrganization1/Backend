package com.sparta.actualpractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://main.daegm2i4mn3we.amplifyapp.com")
                .allowedMethods("*")
                .exposedHeaders("Authorization", "Refresh-Token")
                .allowCredentials(true);
    }
}