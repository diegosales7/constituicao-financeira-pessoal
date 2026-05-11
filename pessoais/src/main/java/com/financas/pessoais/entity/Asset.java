package com.financas.pessoais.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "category", nullable = false)
    private String category; // "RENDA_FIXA" ou "RENDA_VARIAVEL"

    @Column(name = "subcategory", nullable = false)
    private String subcategory; // "TESOURO_DIRETO", "CDB", "ETF", "FUNDO_IMOBILIARIO"

    @Column(name = "investment_value", precision = 19, scale = 2, nullable = false)
    private BigDecimal investmentValue;

    @Column(name = "investment_date", nullable = false)
    private LocalDateTime investmentDate;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

