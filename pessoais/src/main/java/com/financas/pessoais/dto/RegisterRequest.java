package com.financas.pessoais.dto;

/*
 * DTO usado no cadastro de usuário.
 *
 * DTO significa Data Transfer Object.
 * Ele recebe os dados enviados pelo front-end no cadastro.
 *
 * Exemplo de JSON esperado:
 * {
 *   "nome": "Diego",
 *   "email": "diego@email.com",
 *   "idade": 30,
 *   "senha": "1234"
 * }
 */
public class RegisterRequest {

    private String nome;
    private String email;
    private Integer idade;
    private String senha;

    public RegisterRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}