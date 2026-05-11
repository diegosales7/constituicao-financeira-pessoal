package com.financas.pessoais.dto;

import java.math.BigDecimal;

/*
 * DTO de resposta do perfil do usuário.
 *
 * Ele representa os dados que o backend devolve para o front-end.
 * É melhor devolver um DTO do que devolver diretamente a entidade Usuario.
 */
public class UserProfileResponse {

    private Long id;
    private String nome;
    private String email;
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

    public UserProfileResponse() {
    }

    public UserProfileResponse(
            Long id,
            String nome,
            String email,
            Integer idade,
            String telefone,
            String cpf,
            String dataNascimento,
            String profissao,
            String empresa,
            String cargo,
            BigDecimal rendaMensal,
            String endereco,
            String cidade,
            String estado,
            String cep
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.idade = idade;
        this.telefone = telefone;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.profissao = profissao;
        this.empresa = empresa;
        this.cargo = cargo;
        this.rendaMensal = rendaMensal;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
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