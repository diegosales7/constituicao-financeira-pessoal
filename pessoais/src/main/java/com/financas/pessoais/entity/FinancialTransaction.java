package com.financas.pessoais.entity;

import com.financas.pessoais.model.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário dono da transação
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Tipo: RECEITA, DESPESA, INVESTIMENTO, RESERVA_FLEX etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    // Descrição da movimentação
    @Column(nullable = false)
    private String description;

    // Categoria: salário, obra, alimentação, transporte, FII, ETF etc.
    private String category;

    // Valor da transação
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // Data da movimentação
    @Column(nullable = false)
    private LocalDateTime transactionDate;

    // Se essa movimentação entra ou não no cálculo constitucional
    private Boolean affectsConstitution;

    // Observação opcional
    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }

        if (affectsConstitution == null) {
            affectsConstitution = true;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}