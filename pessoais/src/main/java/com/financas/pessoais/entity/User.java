package com.financas.pessoais.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/*
 * Entidade User
 *
 * Essa classe representa o usuário no banco de dados.
 *
 * Ela atende tanto:
 * - autenticação/login
 * - perfil financeiro
 * - análise financeira
 * - Spring Security
 */
@Entity
@Table(name = "usuarios")
public class User {

    /*
     * ID gerado automaticamente pelo banco.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Dados básicos do usuário.
     */
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    /*
     * Campo interno da senha.
     *
     * O nome do campo fica "senha", mas criamos também
     * getPassword() e setPassword() para o Spring Security.
     */
    @Column(nullable = false)
    private String senha;

    private Integer idade;

    /*
     * Papel do usuário no sistema.
     *
     * Exemplo:
     * USER
     * ADMIN
     */
    private String role = "USER";

    /*
     * Dados pessoais.
     */
    private String telefone;

    private String cpf;

    private String dataNascimento;

    /*
     * Dados profissionais.
     */
    private String profissao;

    private String empresa;

    private String cargo;

    private BigDecimal rendaMensal;

    /*
     * Dados financeiros usados pelo FinancialAnalysisService.
     */
    private BigDecimal patrimonio;

    private BigDecimal metaAnual;

    /*
     * Endereço.
     */
    private String endereco;

    private String cidade;

    private String estado;

    private String cep;

    /*
     * Construtor vazio obrigatório para o JPA/Hibernate.
     */
    public User() {
    }

    /*
     * ID
     */
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
     * Senha em português.
     *
     * Usado pelo AuthService atual.
     */
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /*
     * Senha em inglês.
     *
     * Usado pelo Spring Security / SecurityUserDetailsService.
     *
     * Isso resolve o erro:
     * cannot find symbol method getPassword()
     */
    public String getPassword() {
        return senha;
    }

    public void setPassword(String password) {
        this.senha = password;
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
     * Role / Permissão do usuário.
     *
     * Isso resolve o erro:
     * cannot find symbol method getRole()
     */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
     * Patrimônio atual.
     *
     * Usado pelo FinancialAnalysisService.
     *
     * Isso resolve o erro:
     * cannot find symbol method getPatrimonio()
     */
    public BigDecimal getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(BigDecimal patrimonio) {
        this.patrimonio = patrimonio;
    }

    /*
     * Meta anual.
     *
     * Usado pelo FinancialAnalysisService.
     *
     * Isso resolve o erro:
     * cannot find symbol method getMetaAnual()
     */
    public BigDecimal getMetaAnual() {
        return metaAnual;
    }

    public void setMetaAnual(BigDecimal metaAnual) {
        this.metaAnual = metaAnual;
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