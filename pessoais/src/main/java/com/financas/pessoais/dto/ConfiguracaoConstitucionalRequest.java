package com.financas.pessoais.dto;

import java.time.LocalDate;

/*
 * DTO usado para atualizar os percentuais da Constituição.
 */
public class ConfiguracaoConstitucionalRequest {

    private Integer normalInvestimento;
    private Integer normalReservaFlex;
    private Integer normalVida;

    private Integer gatilho1Investimento;
    private Integer gatilho1ReservaFlex;
    private Integer gatilho1Vida;

    private Integer gatilho2Investimento;
    private Integer gatilho2ReservaFlex;
    private Integer gatilho2Vida;

    private LocalDate proximaRevisao;

    public Integer getNormalInvestimento() {
        return normalInvestimento;
    }

    public void setNormalInvestimento(Integer normalInvestimento) {
        this.normalInvestimento = normalInvestimento;
    }

    public Integer getNormalReservaFlex() {
        return normalReservaFlex;
    }

    public void setNormalReservaFlex(Integer normalReservaFlex) {
        this.normalReservaFlex = normalReservaFlex;
    }

    public Integer getNormalVida() {
        return normalVida;
    }

    public void setNormalVida(Integer normalVida) {
        this.normalVida = normalVida;
    }

    public Integer getGatilho1Investimento() {
        return gatilho1Investimento;
    }

    public void setGatilho1Investimento(Integer gatilho1Investimento) {
        this.gatilho1Investimento = gatilho1Investimento;
    }

    public Integer getGatilho1ReservaFlex() {
        return gatilho1ReservaFlex;
    }

    public void setGatilho1ReservaFlex(Integer gatilho1ReservaFlex) {
        this.gatilho1ReservaFlex = gatilho1ReservaFlex;
    }

    public Integer getGatilho1Vida() {
        return gatilho1Vida;
    }

    public void setGatilho1Vida(Integer gatilho1Vida) {
        this.gatilho1Vida = gatilho1Vida;
    }

    public Integer getGatilho2Investimento() {
        return gatilho2Investimento;
    }

    public void setGatilho2Investimento(Integer gatilho2Investimento) {
        this.gatilho2Investimento = gatilho2Investimento;
    }

    public Integer getGatilho2ReservaFlex() {
        return gatilho2ReservaFlex;
    }

    public void setGatilho2ReservaFlex(Integer gatilho2ReservaFlex) {
        this.gatilho2ReservaFlex = gatilho2ReservaFlex;
    }

    public Integer getGatilho2Vida() {
        return gatilho2Vida;
    }

    public void setGatilho2Vida(Integer gatilho2Vida) {
        this.gatilho2Vida = gatilho2Vida;
    }

    public LocalDate getProximaRevisao() {
        return proximaRevisao;
    }

    public void setProximaRevisao(LocalDate proximaRevisao) {
        this.proximaRevisao = proximaRevisao;
    }
}