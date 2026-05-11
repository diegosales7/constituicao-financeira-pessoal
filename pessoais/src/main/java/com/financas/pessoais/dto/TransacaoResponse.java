package com.financas.pessoais.dto;

import com.financas.pessoais.entity.CategoriaTransacao;
import com.financas.pessoais.entity.TipoTransacao;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransacaoResponse {

    private Long id;

    private TipoTransacao tipo;

    private CategoriaTransacao categoria;

    private BigDecimal valor;

    private LocalDate data;

    private String descricao;

    private BigDecimal investimentoConstitucional;

    private BigDecimal reservaFlex;

    private BigDecimal valorVida;
}