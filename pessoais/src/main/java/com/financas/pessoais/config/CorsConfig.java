package com.financas.pessoais.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS para o front-end.
 *
 * Usa variáveis de ambiente para permitir configuração dinâmica por ambiente:
 * - DESENVOLVIMENTO: localhost:5500, localhost:63342
 * - PRODUÇÃO: domínios configurados em app.cors.allowed-origins
 *
 * Variáveis de ambiente (application.properties):
 * - app.cors.allowed-origins: domínios permitidos (separados por vírgula)
 * - app.cors.allowed-methods: métodos HTTP permitidos
 * - app.cors.allow-credentials: permitir credenciais (cookies, auth headers)
 * - app.cors.max-age: tempo de cache da pré-flight request
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost,http://localhost:80}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String[] origins = java.util.Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);

                String[] methods = java.util.Arrays.stream(allowedMethods.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);

                // Se a lista de origens contém '*' e allowCredentials=true, desabilitar allowCredentials
                boolean finalAllowCredentials = allowCredentials;
                for (String o : origins) {
                    if ("*".equals(o)) {
                        if (finalAllowCredentials) {
                            finalAllowCredentials = false;
                        }
                    }
                }

                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods(methods)
                        .allowedHeaders("*")
                        .allowCredentials(finalAllowCredentials)
                        .maxAge(maxAge);
            }
        };
    }
}