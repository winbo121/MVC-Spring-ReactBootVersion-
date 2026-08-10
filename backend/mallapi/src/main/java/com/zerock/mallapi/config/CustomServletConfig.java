package com.zerock.mallapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") //http://localhost:3000으로 하면 localhost:3000만 들어올수 있습니다.
                .allowedMethods("HEAD","GET","POST","PUT","DELETE","OPTIONS")
                .maxAge(300)
                .allowedHeaders("Authoriztion","Cach-Control","Content-Type");
    }
}
