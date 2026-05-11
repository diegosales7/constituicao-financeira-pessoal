package com.financas.pessoais.service;

import com.financas.pessoais.dto.FinancialAnalysisResponse;
import com.financas.pessoais.entity.Asset;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialAnalysisService {

    private final AssetRepository assetRepository;

    /**
     * Analisa a situação financeira do usuário e fornece recomendações
     */
    public FinancialAnalysisResponse analyzeFinancialHealth(User user) {
        FinancialAnalysisResponse analysis = new FinancialAnalysisResponse();

        // Obter dados do usuário
        BigDecimal patrimony = user.getPatrimonio() != null ? user.getPatrimonio() : BigDecimal.ZERO;
        BigDecimal monthlyIncome = user.getRendaMensal() != null ? user.getRendaMensal() : BigDecimal.ZERO;
        BigDecimal annualGoal = user.getMetaAnual() != null ? user.getMetaAnual() : BigDecimal.ZERO;

        // Calcular total investido
        List<Asset> userAssets = assetRepository.findByUser(user);
        BigDecimal totalAssets = userAssets.stream()
                .map(Asset::getInvestmentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular taxa de poupança
        BigDecimal savingsRate = calculateSavingsRate(patrimony, monthlyIncome);

        // Recomendações personalizadas
        List<String> recommendations = generateRecommendations(user, patrimony, monthlyIncome, totalAssets);

        // Score de saúde financeira
        String healthScore = calculateHealthScore(patrimony, monthlyIncome, totalAssets, annualGoal);

        // Verificar se tem fundo de emergência adequado
        Boolean hasEmergencyFund = hasAdequateEmergencyFund(monthlyIncome, totalAssets);

        // Próximo marco financeiro
        String nextMilestone = calculateNextMilestone(patrimony, annualGoal);

        // Preencher resposta
        analysis.setTotalPatrimony(patrimony);
        analysis.setTotalAssets(totalAssets);
        analysis.setMonthlyIncome(monthlyIncome);
        analysis.setAnnualGoal(annualGoal);
        analysis.setSavingsRate(savingsRate);
        analysis.setInvestmentPercentage(BigDecimal.valueOf(30)); // Percentual recomendado
        analysis.setRecommendations(recommendations);
        analysis.setHealthScore(healthScore);
        analysis.setHasEmergencyFund(hasEmergencyFund);
        analysis.setNextMilestone(nextMilestone);

        return analysis;
    }

    /**
     * Calcula a taxa de poupança do usuário
     */
    private BigDecimal calculateSavingsRate(BigDecimal patrimony, BigDecimal monthlyIncome) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // Taxa de poupança = Patrimônio / (Renda Mensal * 12)
        BigDecimal annualIncome = monthlyIncome.multiply(BigDecimal.valueOf(12));
        return patrimony.divide(annualIncome, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Gera recomendações personalizadas baseado no perfil do usuário
     */
    private List<String> generateRecommendations(User user, BigDecimal patrimony,
                                                   BigDecimal monthlyIncome, BigDecimal totalAssets) {
        List<String> recommendations = new ArrayList<>();

        // Recomendação 1: Fundo de Emergência
        BigDecimal recommendedEmergency = monthlyIncome.multiply(BigDecimal.valueOf(6));
        if (patrimony.compareTo(recommendedEmergency) < 0 && monthlyIncome.compareTo(BigDecimal.ZERO) > 0) {
            recommendations.add("Foque em construir um fundo de emergência com 6 meses de gastos (R$ " +
                    formatCurrency(recommendedEmergency) + ")");
        } else {
            recommendations.add("Você tem um fundo de emergência saudável. Parabéns!");
        }

        // Recomendação 2: Diversificação
        if (totalAssets.compareTo(BigDecimal.ZERO) == 0) {
            recommendations.add("Comece a investir com pequenos aportes em diferentes categorias (Renda Fixa e Variável)");
        } else {
            BigDecimal investmentPercentage = totalAssets.divide(patrimony, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            if (investmentPercentage.compareTo(BigDecimal.valueOf(30)) < 0) {
                recommendations.add("Aumente seus investimentos para atingir 30% do seu patrimônio");
            } else {
                recommendations.add("Seu percentual de investimento está excelente!");
            }
        }

        // Recomendação 3: Educação Financeira
        recommendations.add("Continue estudando sobre finanças pessoais e investimentos");

        // Recomendação 4: Renda Estável
        if (monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            recommendations.add("Trabalhe em estabilizar e aumentar sua renda mensal");
        } else {
            recommendations.add("Procure aumentar sua renda através de educação e novas oportunidades");
        }

        // Recomendação 5: Acompanhamento
        recommendations.add("Revise seus objetivos financeiros mensalmente e ajuste seu plano conforme necessário");

        return recommendations;
    }

    /**
     * Calcula o score de saúde financeira
     */
    private String calculateHealthScore(BigDecimal patrimony, BigDecimal monthlyIncome,
                                        BigDecimal totalAssets, BigDecimal annualGoal) {
        int score = 0;

        // Verificar patrimônio (25 pontos)
        if (patrimony.compareTo(BigDecimal.valueOf(10000)) > 0) {
            score += 25;
        } else if (patrimony.compareTo(BigDecimal.valueOf(5000)) > 0) {
            score += 15;
        } else if (patrimony.compareTo(BigDecimal.ZERO) > 0) {
            score += 10;
        }

        // Verificar renda (25 pontos)
        if (monthlyIncome.compareTo(BigDecimal.valueOf(5000)) > 0) {
            score += 25;
        } else if (monthlyIncome.compareTo(BigDecimal.valueOf(2000)) > 0) {
            score += 15;
        } else if (monthlyIncome.compareTo(BigDecimal.ZERO) > 0) {
            score += 10;
        }

        // Verificar se está investindo (25 pontos)
        if (totalAssets.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal investmentRatio = totalAssets.divide(patrimony, 2, RoundingMode.HALF_UP);
            if (investmentRatio.compareTo(BigDecimal.valueOf(0.30)) > 0) {
                score += 25;
            } else if (investmentRatio.compareTo(BigDecimal.valueOf(0.10)) > 0) {
                score += 15;
            } else {
                score += 10;
            }
        }

        // Verificar se tem meta (25 pontos)
        if (annualGoal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal goalProgress = patrimony.divide(annualGoal, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            if (goalProgress.compareTo(BigDecimal.valueOf(100)) > 0) {
                score += 25;
            } else if (goalProgress.compareTo(BigDecimal.valueOf(50)) > 0) {
                score += 20;
            } else if (goalProgress.compareTo(BigDecimal.valueOf(25)) > 0) {
                score += 15;
            }
        }

        // Classificar saúde financeira
        if (score >= 90) {
            return "EXCELENTE";
        } else if (score >= 70) {
            return "BOA";
        } else if (score >= 50) {
            return "RAZOAVEL";
        } else {
            return "CRITICA";
        }
    }

    /**
     * Verifica se o usuário tem fundo de emergência adequado
     */
    private Boolean hasAdequateEmergencyFund(BigDecimal monthlyIncome, BigDecimal totalAssets) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Fundo adequado = 6 meses de renda
        BigDecimal recommendedFund = monthlyIncome.multiply(BigDecimal.valueOf(6));
        return totalAssets.compareTo(recommendedFund) >= 0;
    }

    /**
     * Calcula o próximo marco financeiro a atingir
     */
    private String calculateNextMilestone(BigDecimal patrimony, BigDecimal annualGoal) {
        BigDecimal[] milestones = {
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(500000),
                BigDecimal.valueOf(1000000)
        };

        for (BigDecimal milestone : milestones) {
            if (patrimony.compareTo(milestone) < 0) {
                return "Próximo marco: R$ " + formatCurrency(milestone) +
                        " (faltam R$ " + formatCurrency(milestone.subtract(patrimony)) + ")";
            }
        }

        if (annualGoal.compareTo(BigDecimal.ZERO) > 0) {
            return "Próxima meta: R$ " + formatCurrency(annualGoal) +
                    " (faltam R$ " + formatCurrency(annualGoal.subtract(patrimony)) + ")";
        }

        return "Você atingiu marcos impressionantes! Continue investindo!";
    }

    /**
     * Formata um valor como moeda
     */
    private String formatCurrency(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
}

