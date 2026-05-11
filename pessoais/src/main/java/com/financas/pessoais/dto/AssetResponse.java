package com.financas.pessoais.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {
    private Long id;
    private String category;
    private String subcategory;
    private BigDecimal investmentValue;
    private LocalDateTime investmentDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

