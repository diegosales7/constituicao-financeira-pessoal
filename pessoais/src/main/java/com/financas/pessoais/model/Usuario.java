package com.financas.pessoais.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/*
 * Entidade Usuario
 *
 * Essa classe representa a tabela usuarios no banco de dados.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Dados básicos
     */
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private Integer idade;

    /*
     * Dados pessoais
     */
    private String telefone;

    private String cpf;

    private String dataNascimento;

    /*
     * Dados profissionais
     */
    private String profissao;

    private String empresa;

    private String cargo;

    private BigDecimal rendaMensal;

    /*
     * Endereço
     */
    private String endereco;

    private String cidade;

    private String estado;

    private String cep;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    /*
     * Nome
     */
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    /*
     * Email
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /*
     * Senha
     */
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    /*
     * Idade
     */
    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }


    /*
     * Telefone
     */
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    /*
     * CPF
     */
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    /*
     * Data de nascimento
     */
    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }


    /*
     * Profissão
     */
    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }


    /*
     * Empresa
     */
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }


    /*
     * Cargo
     */
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }


    /*
     * Renda mensal
     */
    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }


    /*
     * Endereço
     */
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    /*
     * Cidade
     */
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    /*
     * Estado
     */
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    /*
     * CEP
     */
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}