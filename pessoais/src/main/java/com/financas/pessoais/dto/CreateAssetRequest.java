package com.financas.pessoais.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssetRequest {
    private String category; // "RENDA_FIXA" ou "RENDA_VARIAVEL"
    private String subcategory; // "TESOURO_DIRETO", "CDB", "ETF", "FUNDO_IMOBILIARIO"
    private BigDecimal investmentValue;
    private LocalDateTime investmentDate;
    private String description;
}

