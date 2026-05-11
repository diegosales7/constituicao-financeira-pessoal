package com.financas.pessoais.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotivationalPhraseResponse {
    private String phrase; // A frase inspiradora
    private String author; // Autor da frase
    private String category; // Categoria (ex: sucesso, paciência, investimento)
}

