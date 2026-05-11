package com.financas.pessoais.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateOccurrenceRequest {

    private String title;

    private String description;

    private BigDecimal amount;

    private BigDecimal correctionPercent;

    private LocalDateTime occurrenceDate;
}