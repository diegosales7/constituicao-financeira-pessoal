package com.financas.pessoais.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_occurrences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialOccurrence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário dono da ocorrência
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Título curto da ocorrência
    @Column(nullable = false)
    private String title;

    // Descrição detalhada
    @Column(length = 1500)
    private String description;

    // Valor envolvido na ocorrência
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    // Percentual já corrigido
    private BigDecimal correctionPercent;

    // Status textual: REGISTRADA, CORRECAO_EM_PROGRESSO, COMPENSADA
    private String status;

    private LocalDateTime occurrenceDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (occurrenceDate == null) {
            occurrenceDate = LocalDateTime.now();
        }

        if (correctionPercent == null) {
            correctionPercent = BigDecimal.ZERO;
        }

        defineStatus();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
        defineStatus();
    }

    private void defineStatus() {
        if (correctionPercent == null) {
            status = "REGISTRADA";
            return;
        }

        if (correctionPercent.compareTo(BigDecimal.valueOf(70)) >= 0) {
            status = "COMPENSADA";
        } else if (correctionPercent.compareTo(BigDecimal.valueOf(30)) >= 0) {
            status = "CORRECAO_EM_PROGRESSO";
        } else {
            status = "REGISTRADA";
        }
    }
}