package com.financas.pessoais.service;

import com.financas.pessoais.dto.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    /**
     * Obtém as cotações de moedas estrangeiras em relação ao Real (BRL)
     * Usa a API pública Open Exchange Rates
     */
    public ExchangeRateResponse getExchangeRates() {
        try {
            // API pública que não requer autenticação (rates fixas para demo)
            // Em produção, usar uma API com chave como Open Exchange Rates
            String apiUrl = "https://api.exchangerate-api.com/v4/latest/BRL";
            
            // Se a API estiver indisponível, retornar valores padrão
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
            
            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                Map<String, BigDecimal> convertedRates = new HashMap<>();
                
                // Moedas importantes para o Brasil
                String[] importantCurrencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF"};
                
                for (String currency : importantCurrencies) {
                    if (rates.containsKey(currency)) {
                        Object rateObj = rates.get(currency);
                        BigDecimal rate = new BigDecimal(rateObj.toString());
                        convertedRates.put(currency, rate);
                    }
                }
                
                ExchangeRateResponse response1 = new ExchangeRateResponse();
                response1.setBaseCurrency("BRL");
                response1.setRates(convertedRates);
                response1.setTimestamp(System.currentTimeMillis());
                response1.setSuccess(true);
                return response1;
            }
        } catch (Exception e) {
            // Se houver erro, retornar valores padrão
            return getDefaultExchangeRates();
        }
        
        return getDefaultExchangeRates();
    }

    /**
     * Converte um valor de Real para outra moeda
     */
    public BigDecimal convertCurrency(BigDecimal amountInBRL, String targetCurrency) {
        ExchangeRateResponse rates = getExchangeRates();
        
        if (rates.getRates().containsKey(targetCurrency)) {
            return amountInBRL.multiply(rates.getRates().get(targetCurrency));
        }
        
        throw new RuntimeException("Moeda não suportada: " + targetCurrency);
    }

    /**
     * Obtém valores padrão de câmbio (fallback quando API falha)
     * Esses valores são aproximados e servem para testes
     */
    private ExchangeRateResponse getDefaultExchangeRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        
        // Valores aproximados (consultar APIs reais para produção)
        rates.put("USD", new BigDecimal("0.20")); // 1 BRL = 0.20 USD (aproximadamente)
        rates.put("EUR", new BigDecimal("0.18")); // 1 BRL = 0.18 EUR
        rates.put("GBP", new BigDecimal("0.16")); // 1 BRL = 0.16 GBP
        rates.put("JPY", new BigDecimal("29.50")); // 1 BRL = 29.50 JPY
        rates.put("AUD", new BigDecimal("0.30")); // 1 BRL = 0.30 AUD
        rates.put("CAD", new BigDecimal("0.27")); // 1 BRL = 0.27 CAD
        rates.put("CHF", new BigDecimal("0.18")); // 1 BRL = 0.18 CHF
        
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setBaseCurrency("BRL");
        response.setRates(rates);
        response.setTimestamp(System.currentTimeMillis());
        response.setSuccess(true);
        return response;
    }
}

