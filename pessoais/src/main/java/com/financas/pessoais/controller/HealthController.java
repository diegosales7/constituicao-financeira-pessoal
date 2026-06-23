package com.financas.pessoais.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    // O contexto global da aplicação está configurado como /api (server.servlet.context-path=/api)
    // Portanto aqui expomos apenas /health e o endpoint completo será /api/health
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "constituicao-financeira"
        ));
    }
}

