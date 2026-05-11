package com.financas.pessoais.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário dono da transação
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaTransacao categoria;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    private LocalDate data;

    @Column(length = 1000)
    private String descricao;

    // Valores constitucionais calculados automaticamente quando for RECEITA
    @Column(name = "investimento_constitucional", precision = 19, scale = 2)
    private BigDecimal investimentoConstitucional;

    @Column(name = "reserva_flex", precision = 19, scale = 2)
    private BigDecimal reservaFlex;

    @Column(name = "valor_vida", precision = 19, scale = 2)
    private BigDecimal valorVida;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void aoCriar() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (data == null) {
            data = LocalDate.now();
        }
    }

    @PreUpdate
    public void aoAtualizar() {
        updatedAt = LocalDateTime.now();
    }
}