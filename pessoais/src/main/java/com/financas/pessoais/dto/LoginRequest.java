package com.financas.pessoais.dto;

/*
 * DTO usado no login.
 *
 * Recebe o e-mail e a senha enviados pelo front-end.
 */
public class LoginRequest {

    private String email;
    private String senha;

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}