package com.financas.pessoais.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private String baseCurrency; // Moeda base (BRL)
    private Map<String, BigDecimal> rates; // Map com pares de moedas e cotações
    private Long timestamp; // Timestamp da última atualização
    private Boolean success; // Indica se a requisição foi bem-sucedida
}

