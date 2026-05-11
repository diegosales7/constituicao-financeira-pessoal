package com.financas.pessoais.dto;

/*
 * DTO de resposta da autenticação.
 *
 * Essa classe será retornada quando o usuário fizer:
 * - cadastro
 * - login
 *
 * Ela carrega:
 * - token JWT
 * - nome do usuário
 * - e-mail do usuário
 * - idade do usuário
 */
public class AuthResponse {

    private String token;
    private String nome;
    private String email;
    private Integer idade;

    public AuthResponse() {
    }

    public AuthResponse(String token, String nome, String email, Integer idade) {
        this.token = token;
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}