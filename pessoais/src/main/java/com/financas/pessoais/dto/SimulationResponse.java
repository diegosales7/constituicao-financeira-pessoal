package com.financas.pessoais.dto;

import java.math.BigDecimal;

public class SimulationResponse {

    private BigDecimal grossIncome;
    private BigDecimal investment;
    private BigDecimal reserve;
    private BigDecimal tax;

    public BigDecimal getGrossIncome() { return grossIncome; }
    public void setGrossIncome(BigDecimal grossIncome) { this.grossIncome = grossIncome; }

    public BigDecimal getInvestment() { return investment; }
    public void setInvestment(BigDecimal investment) { this.investment = investment; }

    public BigDecimal getReserve() { return reserve; }
    public void setReserve(BigDecimal reserve) { this.reserve = reserve; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }
}