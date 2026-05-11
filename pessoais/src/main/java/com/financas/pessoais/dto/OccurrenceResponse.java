package com.financas.pessoais.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OccurrenceResponse {

    private Long id;

    private String title;

    private String description;

    private BigDecimal amount;

    private BigDecimal correctionPercent;

    private String status;

    private LocalDateTime occurrenceDate;
}