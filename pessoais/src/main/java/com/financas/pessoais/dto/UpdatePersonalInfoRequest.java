package com.financas.pessoais.dto;

import java.math.BigDecimal;

/*
 * DTO usado para atualizar o perfil pessoal/profissional.
 *
 * O front-end envia esses dados para o backend.
 */
public class UpdatePersonalInfoRequest {

    private String nome;
    private Integer idade;

    private String telefone;
    private String cpf;
    private String dataNascimento;

    private String profissao;
    private String empresa;
    private String cargo;
    private BigDecimal rendaMensal;

    private String endereco;
    private String cidade;
    private String estado;
    private String cep;

    public UpdatePersonalInfoRequest() {
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getProfissao() {
        return profissao;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }
}