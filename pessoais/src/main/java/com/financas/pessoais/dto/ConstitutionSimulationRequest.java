package com.financas.pessoais.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConstitutionSimulationRequest {

    // Valor recebido
    private BigDecimal income;

    // Percentual atual da fatura do cartão em relação à renda fixa
    private BigDecimal creditCardPercent;

    // Se está em emergência financeira manualmente
    private Boolean emergencyMode;
}