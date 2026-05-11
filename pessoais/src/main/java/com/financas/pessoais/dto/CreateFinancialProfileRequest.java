package com.financas.pessoais.dto;

import lombok.Data;

@Data
public class CreateFinancialProfileRequest {
    private Double investmentPercent;
    private Double reservePercent;
    private Double taxPercent;
    private Double trigger1;
    private Double trigger2;
    private Boolean emergencyMode;
}
