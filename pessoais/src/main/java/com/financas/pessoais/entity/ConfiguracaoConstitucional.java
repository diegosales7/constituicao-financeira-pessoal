package com.financas.pessoais.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/*
 * Configuração Constitucional por usuário.
 *
 * Aqui ficam os percentuais da Constituição Financeira.
 * A ideia é permitir alteração apenas na data de revisão.
 */
@Entity
@Table(name = "configuracoes_constitucionais")
public class ConfiguracaoConstitucional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Cada usuário terá sua própria configuração constitucional.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /*
     * Normal: gastos < R$ 2.250
     */
    private Integer normalInvestimento = 30;
    private Integer normalReservaFlex = 10;
    private Integer normalVida = 60;

    /*
     * Gatilho 1: gastos >= R$ 2.250
     */
    private Integer gatilho1Investimento = 40;
    private Integer gatilho1ReservaFlex = 10;
    private Integer gatilho1Vida = 50;

    /*
     * Gatilho 2: gastos >= R$ 3.500
     */
    private Integer gatilho2Investimento = 50;
    private Integer gatilho2ReservaFlex = 10;
    private Integer gatilho2Vida = 40;

    /*
     * Data em que o usuário poderá alterar a Constituição.
     */
    private LocalDate proximaRevisao = LocalDate.of(2026, 8, 1);

    public ConfiguracaoConstitucional() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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