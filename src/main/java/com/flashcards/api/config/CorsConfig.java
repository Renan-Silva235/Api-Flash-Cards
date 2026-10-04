package com.flashcards.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Origens (sites) que podem chamar a API com cookie, separadas por vírgula.
    // Em produção, defina CORS_ALLOWED_ORIGINS no Render, ex.:
    //   CORS_ALLOWED_ORIGINS=https://seu-app.vercel.app
    // Aceita padrões, ex.: https://seu-app-*.vercel.app (previews da Vercel)
    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:3000}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // allowedOriginPatterns (e não allowedOrigins) para aceitar padrões com "*"
                // junto com allowCredentials(true)
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
