package com.financas.pessoais.controller;

import com.financas.pessoais.dto.ExchangeRateResponse;
import com.financas.pessoais.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cambio")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    /**
     * Obtém as cotações de moedas estrangeiras em relação ao Real (BRL)
     */
    @GetMapping("/taxas")
    public ResponseEntity<ExchangeRateResponse> getExchangeRates() {
        ExchangeRateResponse response = exchangeRateService.getExchangeRates();
        return ResponseEntity.ok(response);
    }

    /**
     * Converte um valor em Real para outra moeda
     */
    @GetMapping("/converter")
    public ResponseEntity<String> convertCurrency(
            @RequestParam BigDecimal valor,
            @RequestParam String moeda) {
        
        try {
            BigDecimal convertedValue = exchangeRateService.convertCurrency(valor, moeda);
            
            String result = String.format(
                    "R$ %.2f = %.2f %s",
                    valor, convertedValue, moeda
            );
            
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    /**
     * Obtém apenas as moedas suportadas
     */
    @GetMapping("/moedas-suportadas")
    public ResponseEntity<String[]> getSupportedCurrencies() {
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF"};
        return ResponseEntity.ok(currencies);
    }
}

