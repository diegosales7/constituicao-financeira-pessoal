package com.financas.pessoais.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PjAllocateRequest {
    private Long empresaId;
    private BigDecimal amount;
}
