package com.financas.pessoais.service;

import com.financas.pessoais.dto.ConstitutionSimulationRequest;
import com.financas.pessoais.dto.ConstitutionSimulationResponse;
import com.financas.pessoais.dto.CreateFinancialProfileRequest;
import com.financas.pessoais.dto.SimulationResponse;
import com.financas.pessoais.entity.FinancialProfile;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.model.ConstitutionStatus;
import com.financas.pessoais.repository.FinancialProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialConstitutionService {

    private final FinancialProfileRepository profileRepository;

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public FinancialProfile createProfile(User user, CreateFinancialProfileRequest request) {
        FinancialProfile profile = profileRepository.findByUser(user).orElse(new FinancialProfile());
        profile.setUser(user);

        profile.setInvestmentPercent(request.getInvestmentPercent() != null
                ? BigDecimal.valueOf(request.getInvestmentPercent())
                : BigDecimal.valueOf(30));

        profile.setReservePercent(request.getReservePercent() != null
                ? BigDecimal.valueOf(request.getReservePercent())
                : BigDecimal.valueOf(10));

        profile.setTaxPercent(request.getTaxPercent() != null
                ? BigDecimal.valueOf(request.getTaxPercent())
                : BigDecimal.valueOf(1.5));

        profile.setTrigger1(request.getTrigger1() != null
                ? BigDecimal.valueOf(request.getTrigger1())
                : BigDecimal.valueOf(50));

        profile.setTrigger2(request.getTrigger2() != null
                ? BigDecimal.valueOf(request.getTrigger2())
                : BigDecimal.valueOf(70));

        profile.setEmergencyMode(request.getEmergencyMode() != null
                ? request.getEmergencyMode()
                : false);

        profile.setDescription("Constituição Financeira Pessoal - Versão Maio/2026");

        return profileRepository.save(profile);
    }

    public SimulationResponse simulate(User user, BigDecimal income) {

        FinancialProfile profile = getOrCreateDefaultProfile(user);

        BigDecimal tax = calculatePercent(income, profile.getTaxPercent());
        BigDecimal investment = calculatePercent(income, profile.getInvestmentPercent());
        BigDecimal reserve = calculatePercent(income, profile.getReservePercent());

        SimulationResponse response = new SimulationResponse();
        response.setGrossIncome(income);
        response.setTax(tax);
        response.setInvestment(investment);
        response.setReserve(reserve);

        return response;
    }

    public ConstitutionSimulationResponse simulateMayConstitution(
            User user,
            ConstitutionSimulationRequest request
    ) {
        BigDecimal income = safe(request.getIncome());
        BigDecimal creditCardPercent = safe(request.getCreditCardPercent());
        boolean emergencyMode = Boolean.TRUE.equals(request.getEmergencyMode());

        ConstitutionStatus status = defineStatus(creditCardPercent, emergencyMode);

        BigDecimal investmentPercent;
        BigDecimal reserveFlexPercent = BigDecimal.valueOf(10);
        BigDecimal lifePercent;

        if (status == ConstitutionStatus.GATILHO_2 || status == ConstitutionStatus.EMERGENCIA) {
            investmentPercent = BigDecimal.valueOf(70);
            lifePercent = BigDecimal.valueOf(20);
        } else if (status == ConstitutionStatus.GATILHO_1) {
            investmentPercent = BigDecimal.valueOf(50);
            lifePercent = BigDecimal.valueOf(40);
        } else {
            investmentPercent = BigDecimal.valueOf(30);
            lifePercent = BigDecimal.valueOf(60);
        }

        BigDecimal investmentAmount = calculatePercent(income, investmentPercent);
        BigDecimal reserveFlexAmount = calculatePercent(income, reserveFlexPercent);
        BigDecimal lifeAmount = calculatePercent(income, lifePercent);

        BigDecimal quarterlyTax = calculatePercent(income, BigDecimal.valueOf(1.5));
        BigDecimal annualTax = calculatePercent(income, BigDecimal.valueOf(1.5));

        List<String> alerts = generateAlerts(status, creditCardPercent);
        List<String> recommendations = generateRecommendations(status);

        return ConstitutionSimulationResponse.builder()
                .grossIncome(income)
                .status(status)
                .investmentPercent(investmentPercent)
                .reserveFlexPercent(reserveFlexPercent)
                .lifePercent(lifePercent)
                .investmentAmount(investmentAmount)
                .reserveFlexAmount(reserveFlexAmount)
                .lifeAmount(lifeAmount)
                .quarterlyDisciplinaryTax(quarterlyTax)
                .annualDisciplinaryTax(annualTax)
                .constitutionalRule("Recebeu → Separou → Investiu → Depois gastou")
                .alerts(alerts)
                .recommendations(recommendations)
                .build();
    }

    private ConstitutionStatus defineStatus(BigDecimal creditCardPercent, boolean emergencyMode) {
        if (emergencyMode) {
            return ConstitutionStatus.EMERGENCIA;
        }

        if (creditCardPercent.compareTo(BigDecimal.valueOf(70)) >= 0) {
            return ConstitutionStatus.GATILHO_2;
        }

        if (creditCardPercent.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return ConstitutionStatus.GATILHO_1;
        }

        return ConstitutionStatus.NORMAL;
    }

    private List<String> generateAlerts(ConstitutionStatus status, BigDecimal creditCardPercent) {
        List<String> alerts = new ArrayList<>();

        if (creditCardPercent.compareTo(BigDecimal.valueOf(35)) > 0) {
            alerts.add("Atenção: parcelas do cartão acima de 35% da renda fixa.");
        }

        if (creditCardPercent.compareTo(BigDecimal.valueOf(50)) >= 0) {
            alerts.add("Gatilho 1 ativado: aumentar investimento para 50% e reduzir vida para 40%.");
        }

        if (creditCardPercent.compareTo(BigDecimal.valueOf(70)) >= 0) {
            alerts.add("Gatilho 2 ativado: aumentar investimento para 70% e reduzir vida para 20%.");
        }

        if (creditCardPercent.compareTo(BigDecimal.valueOf(80)) >= 0) {
            alerts.add("Alerta máximo: cartão próximo ou acima do limite constitucional de 80%.");
        }

        if (status == ConstitutionStatus.EMERGENCIA) {
            alerts.add("Modo emergência financeira ativo: suspender gastos não essenciais.");
        }

        if (alerts.isEmpty()) {
            alerts.add("Situação constitucional normal.");
        }

        return alerts;
    }

    private List<String> generateRecommendations(ConstitutionStatus status) {
        List<String> recommendations = new ArrayList<>();

        recommendations.add("Aplicar a regra: Recebeu → Separou → Investiu → Depois gastou.");
        recommendations.add("Separar 10% para Reserva Flex antes de consumir.");
        recommendations.add("Registrar movimentações importantes no extrato do sistema.");

        if (status == ConstitutionStatus.NORMAL) {
            recommendations.add("Manter 30% para investimentos, priorizando equilíbrio entre FIIs e ETFs.");
        }

        if (status == ConstitutionStatus.GATILHO_1) {
            recommendations.add("Reduzir gastos de vida e acelerar correção financeira.");
        }

        if (status == ConstitutionStatus.GATILHO_2 || status == ConstitutionStatus.EMERGENCIA) {
            recommendations.add("Evitar novas parcelas no cartão e focar em recomposição financeira.");
        }

        return recommendations;
    }

    private FinancialProfile getOrCreateDefaultProfile(User user) {
        return profileRepository.findByUser(user).orElseGet(() -> {
            FinancialProfile profile = new FinancialProfile();
            profile.setUser(user);
            profile.setInvestmentPercent(BigDecimal.valueOf(30));
            profile.setReservePercent(BigDecimal.valueOf(10));
            profile.setTaxPercent(BigDecimal.valueOf(1.5));
            profile.setTrigger1(BigDecimal.valueOf(50));
            profile.setTrigger2(BigDecimal.valueOf(70));
            profile.setEmergencyMode(false);
            profile.setDescription("Constituição Financeira Pessoal - Versão Maio/2026");
            return profileRepository.save(profile);
        });
    }

    private BigDecimal calculatePercent(BigDecimal value, BigDecimal percent) {
        return value.multiply(percent)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}