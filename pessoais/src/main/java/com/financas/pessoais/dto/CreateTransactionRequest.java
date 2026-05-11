package com.financas.pessoais.dto;

import com.financas.pessoais.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTransactionRequest {

    private TransactionType type;

    private String description;

    private String category;

    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private Boolean affectsConstitution;

    private String notes;
}