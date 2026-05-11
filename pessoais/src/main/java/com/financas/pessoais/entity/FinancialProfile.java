package com.financas.pessoais.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Percentuais de Constituição =====
    private BigDecimal investmentPercent;      // ex: 30%
    private BigDecimal reservePercent;         // ex: 10%
    private BigDecimal taxPercent;             // ex: 5%

    // ===== Gatilhos Financeiros =====
    private BigDecimal trigger1;               // gatilho 1 (alertas financeiros)
    private BigDecimal trigger2;               // gatilho 2

    // ===== Status e Datas =====
    private Boolean emergencyMode;             // modo emergência ativado?
    private LocalDateTime createdAt;           // data de criação
    private LocalDateTime updatedAt;           // data da última atualização

    // ===== Metadata =====
    private String description;                // descrição da constituição

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @SuppressWarnings("unused")
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @SuppressWarnings("unused")
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
