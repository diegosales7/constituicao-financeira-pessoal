package com.financas.pessoais.dto;

import com.financas.pessoais.model.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionResponse {

    private Long id;

    private TransactionType type;

    private String description;

    private String category;

    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private Boolean affectsConstitution;

    private String notes;
}