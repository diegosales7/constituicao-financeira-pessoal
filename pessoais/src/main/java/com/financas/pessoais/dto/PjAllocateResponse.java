package com.financas.pessoais.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PjAllocateResponse {
    private BigDecimal operating;
    private BigDecimal reserves;
    private BigDecimal investments;
}
