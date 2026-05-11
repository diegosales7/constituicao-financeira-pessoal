package com.financas.pessoais.dto;

import com.financas.pessoais.model.ConstitutionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class ConstitutionSimulationResponse {

    private BigDecimal grossIncome;

    private ConstitutionStatus status;

    private BigDecimal investmentPercent;
    private BigDecimal reserveFlexPercent;
    private BigDecimal lifePercent;

    private BigDecimal investmentAmount;
    private BigDecimal reserveFlexAmount;
    private BigDecimal lifeAmount;

    private BigDecimal quarterlyDisciplinaryTax;
    private BigDecimal annualDisciplinaryTax;

    private String constitutionalRule;

    private List<String> alerts;
    private List<String> recommendations;
}