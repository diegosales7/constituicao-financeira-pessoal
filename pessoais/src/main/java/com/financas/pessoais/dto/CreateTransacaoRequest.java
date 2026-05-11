package com.financas.pessoais.dto;

import com.financas.pessoais.entity.CategoriaTransacao;
import com.financas.pessoais.entity.TipoTransacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransacaoRequest {

    private TipoTransacao tipo;

    private CategoriaTransacao categoria;

    private BigDecimal valor;

    private LocalDate data;

    private String descricao;
}