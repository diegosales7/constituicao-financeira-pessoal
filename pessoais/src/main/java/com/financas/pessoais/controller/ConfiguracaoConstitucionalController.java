package com.financas.pessoais.controller;

import com.financas.pessoais.dto.ConfiguracaoConstitucionalRequest;
import com.financas.pessoais.entity.ConfiguracaoConstitucional;
import com.financas.pessoais.service.ConfiguracaoConstitucionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Controller da Configuração Constitucional.
 *
 * Por enquanto recebe o email por parâmetro.
 * Depois, quando o JWT estiver completo, dá para buscar pelo usuário logado.
 */
@RestController
@RequestMapping("/configuracao-constitucional")
public class ConfiguracaoConstitucionalController {

    private final ConfiguracaoConstitucionalService service;

    public ConfiguracaoConstitucionalController(ConfiguracaoConstitucionalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ConfiguracaoConstitucional> buscar(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(service.buscarOuCriar(email));
    }

    @PutMapping
    public ResponseEntity<ConfiguracaoConstitucional> atualizar(
            @RequestParam String email,
            @RequestBody ConfiguracaoConstitucionalRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(email, request));
    }
}