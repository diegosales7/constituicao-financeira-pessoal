package com.financas.pessoais.controller;

import com.financas.pessoais.dto.MotivationalPhraseResponse;
import com.financas.pessoais.service.MotivationalPhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frases")
@RequiredArgsConstructor
public class MotivationalPhraseController {

    private final MotivationalPhraseService phraseService;

    /**
     * Obtém uma frase inspiradora aleatória
     */
    @GetMapping("/aleatoria")
    public ResponseEntity<MotivationalPhraseResponse> getRandomPhrase() {
        MotivationalPhraseResponse phrase = phraseService.getRandomPhrase();
        return ResponseEntity.ok(phrase);
    }

    /**
     * Obtém uma frase por categoria
     */
    @GetMapping("/categoria/{category}")
    public ResponseEntity<MotivationalPhraseResponse> getPhraseByCategory(
            @PathVariable String category) {
        MotivationalPhraseResponse phrase = phraseService.getPhraseByCategory(category);
        return ResponseEntity.ok(phrase);
    }

    /**
     * Obtém todas as categorias disponíveis
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = phraseService.getCategories();
        return ResponseEntity.ok(categories);
    }
}

