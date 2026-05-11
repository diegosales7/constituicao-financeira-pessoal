package com.financas.pessoais.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialAnalysisResponse {
    private BigDecimal totalPatrimony; // Patrimônio total do usuário
    private BigDecimal totalAssets; // Total investido em ativos
    private BigDecimal monthlyIncome; // Renda mensal
    private BigDecimal annualGoal; // Meta anual
    private BigDecimal savingsRate; // Taxa de poupança (%)
    private BigDecimal investmentPercentage; // Percentual recomendado de investimento
    private List<String> recommendations; // Lista de recomendações personalizadas
    private String healthScore; // Saúde financeira (Excelente, Boa, Razoável, Crítica)
    private Boolean hasEmergencyFund; // Possui fundo de emergência adequado
    private String nextMilestone; // Próximo marco financeiro a atingir
}

